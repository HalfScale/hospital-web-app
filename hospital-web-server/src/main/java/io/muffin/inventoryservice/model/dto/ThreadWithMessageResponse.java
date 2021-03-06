package io.muffin.inventoryservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThreadWithMessageResponse extends ThreadResponse{

    private String receiverImage;
    private String senderImage;
    private String latestMessage;
}
