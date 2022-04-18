package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.Messages;
import io.muffin.inventoryservice.model.SenderUsers;
import io.muffin.inventoryservice.model.Threads;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.MessagingRequest;
import io.muffin.inventoryservice.repository.MessagesRepository;
import io.muffin.inventoryservice.repository.SenderUserRepository;
import io.muffin.inventoryservice.repository.ThreadsRepository;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
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

import java.util.ArrayList;
import java.util.Optional;

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
        assertNotNull(messagingService.deleteThread("1"));
    }

    @Test
    public void testGetThreadsByLoggedUser() {
        Long loggedUserId = getJwtUserDetails().getId();
        when(authUtil.getCurrentUser()).thenReturn(getJwtUserDetails());
        when(senderUserRepository.findDistinctByReceiverIdOrSenderId(Mockito.anyLong(), Mockito.eq(loggedUserId),
                Mockito.eq(PageRequest.of(0, 20)))).thenReturn(new PageImpl(new ArrayList()));
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
}
