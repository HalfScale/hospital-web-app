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

    public ResponseEntity<Object> sendMesage(MessagingRequest messagingRequest) throws JsonProcessingException {
        // create a message and a thread
        Messages messages = this.mapToMessageEntity(messagingRequest);

        log.info("Message to be saved => [{}]", objectMapper.writeValueAsString(messages));
        Messages savedMessage = messagesRepository.save(messages);
        return new ResponseEntity(savedMessage.getId(), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getThread(String receiverId, Pageable pageable) {
        Page<Thread> messageThread = threadsRepository.findByReceiverIdAndSenderId(Long.valueOf(receiverId), authUtil.getCurrentUser().getId(), pageable);
        return ResponseEntity.ok(messageThread);
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
        messages.setThreads(thread);
        messages.setMessage(messagingRequest.getMessage());
        messages.setCreated(currentDateTime);
        return messages;
    }
}
