package com.muffin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffin.jwt.JwtUserDetails;
import com.muffin.model.Messages;
import com.muffin.model.SenderUsers;
import com.muffin.model.Threads;
import com.muffin.model.UserDetails;
import com.muffin.model.dto.MessagingRequest;
import com.muffin.repository.MessagesRepository;
import com.muffin.repository.SenderUserRepository;
import com.muffin.repository.ThreadsRepository;
import com.muffin.repository.UserDetailsRepository;
import com.muffin.utility.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MessagingServiceTest {

    private @Mock
    ThreadsRepository threadsRepository;
    private @Mock
    MessagesRepository messagesRepository;
    private @Mock
    AuthUtil authUtil;
    private @Mock
    ObjectMapper objectMapper;
    private @Mock
    SenderUserRepository senderUserRepository;
    private @Mock
    UserDetailsRepository userDetailsRepository;
    private @InjectMocks
    MessagingService messagingService;

    @Test
    public void testSendMessage() throws JsonProcessingException {
        when(messagesRepository.save((Mockito.any(Messages.class)))).thenReturn(getMessage());
        when(senderUserRepository.save(Mockito.any(SenderUsers.class))).thenReturn(getSenderUser());
        when(authUtil.getCurrentUser()).thenReturn(getJwtUserDetails());
        assertNotNull(messagingService.sendMessage(getNewMessagingRequest()));
    }

    @Test
    public void testSendMessage_ToExistingThread() throws JsonProcessingException {
        when(messagesRepository.save((Mockito.any(Messages.class)))).thenReturn(getMessage());
        when(senderUserRepository.save(Mockito.any(SenderUsers.class))).thenReturn(getSenderUser());
        when(threadsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(this.getThread()));
        when(authUtil.getCurrentUser()).thenReturn(getJwtUserDetails());
        assertNotNull(messagingService.sendMessage(getExistingMessagingRequest()));
    }

    @Test
    public void testDeleteThread() {
        when(threadsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Threads()));
        when(threadsRepository.save(Mockito.any(Threads.class))).thenReturn(new Threads());
        assertNotNull(messagingService.deleteThread(this.getThreadMap()));
    }

    @Test
    public void testGetThreadsByLoggedUser() {
        Long loggedUserId = getJwtUserDetails().getId();
        when(authUtil.getCurrentUser()).thenReturn(getJwtUserDetails());
        when(senderUserRepository.findDistinctByReceiverIdOrSenderId(Mockito.anyLong(), Mockito.eq(loggedUserId),
                Mockito.eq(PageRequest.of(0, 20)))).thenReturn(new PageImpl(new ArrayList()));
        when(userDetailsRepository.findByUsersId(Mockito.any())).thenReturn(Optional.of(this.getUserDetails()));
        when(messagesRepository.findFirstBySenderUsersThreadIdOrderByIdDesc(Mockito.anyLong())).thenReturn(Optional.of(this.getMessage()));
        assertNotNull(messagingService.getThreadsByLoggedUser(PageRequest.of(0, 20)));
    }

    @Test
    public void testGetMessagesByThreadId() throws JsonProcessingException {
        when(messagesRepository.findByThreadId(Mockito.anyLong(), Mockito.eq(PageRequest.of(0, 20))))
                .thenReturn(new PageImpl(new ArrayList()));
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(this.getUserDetails()));
        assertNotNull(messagingService.getMessagesByThreadId("1", PageRequest.of(0, 20)));
    }

    private JwtUserDetails getJwtUserDetails() {
        return new JwtUserDetails(1L, "email", "password", "name", "role");
    }

    private UserDetails getUserDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName("");
        userDetails.setLastName("");
        return  userDetails;
    }

    private Threads getThread() {
        Threads thread = new Threads();
        thread.setId(1L);
        return thread;
    }
    private SenderUsers getSenderUser() {
        SenderUsers senderUsers = new SenderUsers();
        senderUsers.setId(1L);
        senderUsers.setSenderId(1L);
        senderUsers.setReceiverId(1L);
        senderUsers.setThread(this.getThread());
        return senderUsers;
    }

    private Messages getMessage() {
        Messages message = new Messages();
        message.setId(1L);
        message.setSenderUsers(this.getSenderUser());
        return message;
    }

    private MessagingRequest getNewMessagingRequest() {
        MessagingRequest messagingRequest = new MessagingRequest();
        messagingRequest.setThreadId(-1L);
        return messagingRequest;
    }

    private MessagingRequest getExistingMessagingRequest() {
        MessagingRequest messagingRequest = new MessagingRequest();
        messagingRequest.setThreadId(1L);
        return messagingRequest;
    }

    private Map<String, List<String>> getThreadMap() {
        Map threadMap = new HashMap();
        List<String> threadIds = Arrays.asList("1", "2", "3");
        threadMap.put("threads", threadIds);
        return threadMap;
    }
}
