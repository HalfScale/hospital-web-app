package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.Messages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {
    @Query("SELECT messages FROM Messages messages WHERE messages.senderUsers.thread.id = ?1 " +
            "AND messages.senderUsers.thread.deleted = 0")
    Page<Messages> findByThreadId(Long threadId, Pageable pageable);

    @Query("SELECT messages FROM Messages messages WHERE messages.senderUsers.receiverId = ?1 " +
            "AND messages.senderUsers.senderId = ?2 AND messages.senderUsers.thread.deleted = 0")
    Page<Messages> findByReceiverIdAndSenderId(Long receiverId, Long senderId, Pageable pageable);
}
