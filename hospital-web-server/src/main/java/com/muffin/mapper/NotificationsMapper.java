package com.muffin.mapper;

import com.muffin.model.Notifications;
import com.muffin.model.UserDetails;
import com.muffin.model.Users;
import com.muffin.model.dto.NotificationResponse;
import com.muffin.utility.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class NotificationsMapper {

    public NotificationResponse mapToNotificationResponse(Notifications notifications, UserDetails userDetails) {
        Users users = userDetails.getUsers();

        UserDetails sender = this.getNotificationSender(notifications, userDetails);

        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setSenderFirstName(sender.getFirstName());
        notificationResponse.setSenderLastName(sender.getLastName());
        notificationResponse.setSenderImage(sender.getProfileImage());
        notificationResponse.setId(notifications.getId());
        notificationResponse.setAppointmentId(notifications.getAppointments().getId());
        notificationResponse.setStatus(notifications.getStatus());
        notificationResponse.setMessage(notifications.getMessage());
        notificationResponse.setViewed(notifications.getViewed());
        return notificationResponse;
    }

    private UserDetails getNotificationSender(Notifications notifications, UserDetails userDetails) {
        Users users = userDetails.getUsers();
        UserDetails sender = null;
        if(users.getUserType() == Constants.USER_PATIENT) {
            sender = notifications.getAppointments().getDoctor();
        }else {
            sender = notifications.getAppointments().getPatient();
        }
        return sender;
    }
}
