package io.muffin.inventoryservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "sender_users_id", referencedColumnName = "id")
    private SenderUsers senderUsers;
    private String message;
    private LocalDateTime created;
    private LocalDateTime modified;
}
