package io.muffin.inventoryservice.mapper;

import io.muffin.inventoryservice.model.Notifications;
import io.muffin.inventoryservice.model.dto.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationsMapper {

    public NotificationResponse mapToNotificationResponse(Notifications notifications) {
        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setId(notifications.getId());
        notificationResponse.setAppointmentId(notifications.getAppointment().getId());
        notificationResponse.setStatus(notifications.getStatus());
        notificationResponse.setMessage(notifications.getMessage());
        notificationResponse.setViewed(notifications.getViewed());
        return notificationResponse;
    }
}
