package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {
}
