package io.muffin.inventoryservice.model.dto;

import io.muffin.inventoryservice.model.Threads;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThreadResponse {

    private Long threadId;
    private Long receiverId;
    private Long senderId;
    private String receiverName;
    private String senderName;
}
