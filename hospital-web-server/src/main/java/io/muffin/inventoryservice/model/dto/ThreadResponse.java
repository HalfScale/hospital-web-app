package io.muffin.inventoryservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThreadResponse {

    private Long id;
    private Long receiverId;
    private Long senderId;
    private String receiverName;
    private String senderName;
}
