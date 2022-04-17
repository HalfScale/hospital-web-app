package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.exception.HospitalException;
import io.muffin.inventoryservice.model.Messages;
import io.muffin.inventoryservice.model.SenderUsers;
import io.muffin.inventoryservice.model.Threads;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.MessagingRequest;
import io.muffin.inventoryservice.model.dto.ThreadResponse;
import io.muffin.inventoryservice.repository.*;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import io.muffin.inventoryservice.utility.SystemUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class MessagingService {

    private final UserDetailsRepository userDetailsRepository;
    private final ThreadsRepository threadsRepository;
    private final MessagesRepository messagesRepository;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final SenderUserRepository senderUserRepository;

    public ResponseEntity<Object> sendMessage(MessagingRequest messagingRequest) throws JsonProcessingException {
        Messages messages = this.mapToMessageEntity(messagingRequest);
        Messages savedMessage = messagesRepository.save(messages);
        Long threadId = savedMessage.getSenderUsers().getThread().getId();
        return new ResponseEntity(threadId, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> deleteThread(String threadId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Threads thread = threadsRepository.findById(Long.valueOf(threadId))
                .orElseThrow(() -> new HospitalException("Message thread not existing!"));
        thread.setDeleted(true);
        thread.setDeletedDate(currentDateTime);
        threadsRepository.save(thread);
        return ResponseEntity.ok(threadId);
    }

    public ResponseEntity<Object> getThreadsByLoggedUser(Pageable pageable) {
        Long loggedUserId = authUtil.getCurrentUser().getId();
        Page<ThreadResponse> threads = senderUserRepository.findDistinctByReceiverIdOrSenderId(loggedUserId, loggedUserId, pageable)
                .map(senderUsers -> {
                    try {
                        log.info("senderUsers", objectMapper.writeValueAsString(senderUsers));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    UserDetails receiver = userDetailsRepository.findByUsersId(senderUsers.getReceiverId())
                            .orElseThrow(() -> new HospitalException("User not found!"));
                    UserDetails sender = userDetailsRepository.findByUsersId(senderUsers.getSenderId())
                            .orElseThrow(() -> new HospitalException("User not found!"));
                    String receiverName = String.format("%s %s", receiver.getFirstName(), receiver.getLastName());
                    String senderName = String.format("%s %s", sender.getFirstName(), sender.getLastName());
                    return new ThreadResponse(senderUsers.getThread().getId(), receiver.getUsers().getId(),
                            sender.getUsers().getId(), receiverName, senderName);
                });
        return ResponseEntity.ok(SystemUtil.mapToGenericPageableResponse(threads));
    }

//    public ResponseEntity<Object> getMessagesByThreadId(String threadId, Pageable pageable) throws JsonProcessingException {
//        Page<Messages> messages = messagesRepository.findAllByThreadIdAndThreadDeletedFalse(Long.valueOf(threadId), pageable);
//        return ResponseEntity.ok(SystemUtil.mapToGenericPageableResponse(messages));
//    }

    private Messages mapToMessageEntity(MessagingRequest messagingRequest) throws JsonProcessingException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Threads thread = null;

        if(messagingRequest.getThreadId() == Constants.NEW_ENTITY_ID) {
            Threads newMessageThread = new Threads();
            newMessageThread.setId(messagingRequest.getThreadId());
            newMessageThread.setCreated(currentDateTime);
            newMessageThread.setDeleted(false);

            thread = threadsRepository.save(newMessageThread);
        }else {
            thread = threadsRepository.findById(messagingRequest.getThreadId())
                    .orElseThrow(() -> new HospitalException("Invalid thread id!"));
        }

        SenderUsers senderUser = new SenderUsers();
        senderUser.setSenderId(authUtil.getCurrentUser().getId());
        senderUser.setReceiverId(messagingRequest.getReceiverId());
        senderUser.setThread(thread);
        senderUser.setCreated(currentDateTime);
        senderUser.setModified(currentDateTime);

        SenderUsers savedSenderUser = senderUserRepository.save(senderUser);

        Messages messages = new Messages();
        messages.setSenderUsers(savedSenderUser);
        messages.setMessage(messagingRequest.getMessage());
        messages.setCreated(currentDateTime);
        messages.setModified(currentDateTime);

        log.info("Message to be saved => [{}]", objectMapper.writeValueAsString(messages));
        return messages;
    }
}
