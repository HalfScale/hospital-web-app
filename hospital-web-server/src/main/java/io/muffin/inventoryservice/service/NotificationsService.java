package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.exception.HospitalException;
import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.mapper.NotificationsMapper;
import io.muffin.inventoryservice.model.Appointment;
import io.muffin.inventoryservice.model.Notifications;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.NotificationResponse;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import io.muffin.inventoryservice.utility.SystemUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.muffin.inventoryservice.repository.NotificationsRepository;
import org.yaml.snakeyaml.scanner.Constant;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationsService {

    private final NotificationsRepository notificationsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final AuthUtil authUtil;
    private final NotificationsMapper notificationsMapper;

    public ResponseEntity<Object> findById(String id) {
        return null;
    }

    public ResponseEntity<Object> findAll(Pageable pageable) {
        JwtUserDetails jwtUserDetails = authUtil.getCurrentUser();

        UserDetails currentUser = userDetailsRepository.findByUsersId(jwtUserDetails.getId())
                .orElseThrow(() -> new HospitalException("User not existing!"));
        long currentUserId = currentUser.getUsers().getId();

        Page<NotificationResponse> notificationsResponse = notificationsRepository.findAllByCurrentUser(currentUserId, pageable)
                .map(notifications -> {
                    return notificationsMapper.mapToNotificationResponse(notifications, currentUser);
                });

        return ResponseEntity.ok(SystemUtil.mapToGenericPageableResponse(notificationsResponse));
    }

    public ResponseEntity<Object> countUnviewedNotifications() {
        JwtUserDetails currentUser = authUtil.getCurrentUser();
        long count = notificationsRepository.countUnviewedNotifications(currentUser.getId());
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<Object> setNotificationViewedDate(long id) {
        LocalDateTime today = LocalDateTime.now();
        Notifications notifications = notificationsRepository.findById(id)
                .orElseThrow(() -> new HospitalException("Notification not existing!"));
        notifications.setViewed(today);
        notifications.setModified(today);

        notificationsRepository.save(notifications);

        return ResponseEntity.ok(notifications.getId());
    }

    public long sendNotification(Appointment appointment, UserDetails receiver, int status) {
        Notifications notifications = new Notifications();
        notifications.setAppointment(appointment);
        notifications.setReceiver(receiver);
        notifications.setMessage(this.generateNotificationMessage(status));
        notifications.setStatus(status);
        notifications.setCreated(LocalDateTime.now());
        notifications.setModified(LocalDateTime.now());

        notifications = notificationsRepository.save(notifications);

        return notifications.getId();
    }

    private String generateNotificationMessage(int status) {
        String message = "";
        if(status == Constants.APPOINTMENT_APPROVED) {
            message = "Your appointment has been approved.";
        }else if (status == Constants.APPOINTMENT_CANCELLED) {
            message = "An appointment has been cancelled.";
        }else if (status == Constants.APPOINTMENT_REJECTED) {
            message = "Your appointment has been rejected.";
        }else if (status == Constants.APPOINTMENT_PENDING) {
            message = "An appointment has been created.";
        }
        return message;
    }
}