package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.exception.HospitalException;
import io.muffin.inventoryservice.model.Messages;
import io.muffin.inventoryservice.model.Threads;
import io.muffin.inventoryservice.model.dto.MessagingRequest;
import io.muffin.inventoryservice.repository.MessagesRepository;
import io.muffin.inventoryservice.repository.ThreadsRepository;
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

    private final ThreadsRepository threadsRepository;
    private final MessagesRepository messagesRepository;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;

    public ResponseEntity<Object> sendMessage(MessagingRequest messagingRequest) throws JsonProcessingException {
        Messages messages = this.mapToMessageEntity(messagingRequest);
        log.info("Message to be saved => [{}]", objectMapper.writeValueAsString(messages));
        Messages savedMessage = messagesRepository.save(messages);
        return new ResponseEntity(savedMessage.getThread().getId(), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> deleteThread(String threadId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Threads thread = threadsRepository.findById(Long.valueOf(threadId))
                .orElseThrow(() -> new HospitalException("Message thread not existing!"));
        thread.setDeleted(true);
        thread.setModified(currentDateTime);
        thread.setDeletedDate(currentDateTime);
        threadsRepository.save(thread);
        return ResponseEntity.ok(threadId);
    }

    public ResponseEntity<Object> getMessageThread(String receiverId, Pageable pageable) {
        Page<Threads> messageThread = threadsRepository.findByReceiverIdAndSenderIdAndDeletedFalse(Long.valueOf(receiverId), authUtil.getCurrentUser().getId(), pageable);
        return ResponseEntity.ok(SystemUtil.mapToGenericPageableResponse(messageThread));
    }

    public ResponseEntity<Object> getMessagesByThreadId(String threadId, Pageable pageable) throws JsonProcessingException {
        Page<Messages> messages = messagesRepository.findAllByThreadIdAndThreadDeletedFalse(Long.valueOf(threadId), pageable);
        return ResponseEntity.ok(SystemUtil.mapToGenericPageableResponse(messages));
    }

    private Messages mapToMessageEntity(MessagingRequest messagingRequest) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Threads thread = null;

        if(messagingRequest.getThreadId() == Constants.NEW_ENTITY_ID) {
            Threads newMessageThread = new Threads();
            newMessageThread.setId(messagingRequest.getThreadId());
            newMessageThread.setReceiverId(messagingRequest.getReceiverId());
            newMessageThread.setSenderId(authUtil.getCurrentUser().getId());
            newMessageThread.setCreated(currentDateTime);
            newMessageThread.setModified(currentDateTime);
            newMessageThread.setDeleted(false);

            thread = threadsRepository.save(newMessageThread);
        }else {
            thread = threadsRepository.findById(messagingRequest.getThreadId())
                    .orElseThrow(() -> new HospitalException("Invalid thread id!"));
        }


        Messages messages = new Messages();
        messages.setThread(thread);
        messages.setMessage(messagingRequest.getMessage());
        messages.setCreated(currentDateTime);
        return messages;
    }
}
