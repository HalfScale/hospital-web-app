package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.SenderUsers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SenderUserRepository extends JpaRepository<SenderUsers, Long> {

    @Query("SELECT sender FROM SenderUsers sender WHERE (sender.receiverId = ?1 OR sender.senderId = ?2) " +
            "AND sender.thread.deleted = 0 GROUP BY sender.thread.id")
    Page<SenderUsers> findDistinctByReceiverIdOrSenderId(Long receiverId, Long senderId, Pageable pageable);
}
