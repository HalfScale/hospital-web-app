package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    @Query("SELECT notif FROM Notifications notif WHERE notif.receiver.users.id = ?1 " +
            "AND notif.deleted = 0")
    Page<Notifications> findAllByCurrentUser(long userId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM Notifications n WHERE n.receiver.users.id = ?1 " +
            "AND n.viewed is null AND n.deleted = 0")
    long countUnviewedNotifications(Long userId);
}