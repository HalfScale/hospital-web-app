package io.muffin.inventoryservice.mapper;

import io.muffin.inventoryservice.model.Notifications;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.Users;
import io.muffin.inventoryservice.model.dto.NotificationResponse;
import io.muffin.inventoryservice.utility.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationsMapper {

    public NotificationResponse mapToNotificationResponse(Notifications notifications, UserDetails userDetails) {
        Users users = userDetails.getUsers();

        UserDetails sender = this.getNotificationSender(notifications, userDetails);

        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setSenderFirstName(sender.getFirstName());
        notificationResponse.setSenderLastName(sender.getLastName());
        notificationResponse.setSenderImage(sender.getProfileImage());
        notificationResponse.setId(notifications.getId());
        notificationResponse.setAppointmentId(notifications.getAppointment().getId());
        notificationResponse.setStatus(notifications.getStatus());
        notificationResponse.setMessage(notifications.getMessage());
        notificationResponse.setViewed(notifications.getViewed());
        return notificationResponse;
    }

    private UserDetails getNotificationSender(Notifications notifications, UserDetails userDetails) {
        Users users = userDetails.getUsers();
        UserDetails sender = null;
        if(users.getUserType() == Constants.USER_PATIENT) {
            sender = notifications.getAppointment().getDoctor();
        }else {
            sender = notifications.getAppointment().getPatient();
        }
        return sender;
    }
}
