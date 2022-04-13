package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.Threads;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadsRepository extends JpaRepository<Threads, Long> {

    Page<Threads> findByReceiverIdAndSenderIdAndDeletedFalse(Long receiverId, Long senderId, Pageable peagable);
}
