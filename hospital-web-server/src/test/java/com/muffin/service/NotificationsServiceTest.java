package com.muffin.service;

import com.muffin.jwt.JwtUserDetails;
import com.muffin.model.Appointments;
import com.muffin.model.Notifications;
import com.muffin.model.UserDetails;
import com.muffin.model.Users;
import com.muffin.repository.NotificationsRepository;
import com.muffin.repository.UserDetailsRepository;
import com.muffin.utility.AuthUtil;
import com.muffin.utility.Constants;
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
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NotificationsServiceTest {

    @Mock
    private NotificationsRepository notificationsRepository;
    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private NotificationsService notificationsService;

    @Test
    public void testSendNotification() {
        when(notificationsRepository.save(Mockito.any(Notifications.class)))
                .thenReturn(this.getNotifications());
        assertNotNull(notificationsService.sendNotification(this.getAppointment(), this.getUserDetails(), 1));
    }

    @Test
    public void testSetNotificationViewedDate() {
        when(notificationsRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(this.getNotifications()));

        assertNotNull(notificationsService.setNotificationViewedDate(1));
    }

    @Test
    public void testFindAll() {
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(this.getUserDetails()));
        when(notificationsRepository.findAllByCurrentUser(Mockito.anyLong(), Mockito.eq(Pageable.ofSize(1))))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertNotNull(notificationsService.findAll(Pageable.ofSize(1)));
    }

    private Appointments getAppointment() {
        Appointments appointments = new Appointments();
        appointments.setId(1L);
        appointments.setPatient(this.getUserDetails());
        appointments.setDoctor(this.getUserDetails());
        return appointments;
    }

    private Notifications getNotifications() {
        Notifications notifications = new Notifications();
        notifications.setId(1L);
        return notifications;
    }

    private JwtUserDetails getJwtUserDetails() {
        JwtUserDetails jwtUserDetails = new JwtUserDetails(1L, "email", "password",
                "name", "role");
        return jwtUserDetails;
    }

    private UserDetails getUserDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsers(this.getUsers());
        userDetails.setId(1L);
        userDetails.setFirstName("Jane");
        userDetails.setLastName("Doe");
        userDetails.setAddress("Manila City");
        userDetails.setGender(Constants.FEMALE);
        userDetails.setMobileNo("");
        return userDetails;
    }

    private Users getUsers() {
        Users users = new Users();
        users.setId(1L);
        users.setEmail("janedoe@gmail.com");
        users.setUserType(Constants.USER_PATIENT);
        return users;
    }

}