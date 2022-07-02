package io.muffin.inventoryservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sender_users")
public class SenderUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "thread_id", referencedColumnName = "id")
    private Threads thread;
    private Long receiverId;
    private Long senderId;
    private LocalDateTime created;
    private LocalDateTime modified;
}
