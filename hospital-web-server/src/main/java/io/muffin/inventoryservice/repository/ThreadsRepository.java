package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.Threads;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThreadsRepository extends JpaRepository<Threads, Long> {

    Page<Thread> findByReceiverIdAndSenderId(Long receiverId, Long senderId, Pageable peagable);
}
