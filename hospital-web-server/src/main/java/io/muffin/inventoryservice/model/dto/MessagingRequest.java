package io.muffin.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessagingRequest {

    private Long threadId;
    private Long receiverId;
    private String message;
}
