package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.RoomReservations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface RoomReservationsRepository extends JpaRepository<RoomReservations, Long> {

    Page<RoomReservations> findByRoomCodeAndRoomNameAndReservationStatus(String roomCode,
                                                                         String roomName, Integer status, Pageable pageable);
}