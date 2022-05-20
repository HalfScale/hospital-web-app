package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.RoomReservations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RoomReservationsRepository extends JpaRepository<RoomReservations, Long> {

    Page<RoomReservations> findByRoomCodeAndHospitalRoomRoomNameAndReservationStatusAndDeletedFalse(String roomCode,
                                                                                                    String roomName, Integer status, Pageable pageable);

    @Query("SELECT reservations FROM RoomReservations reservations WHERE reservations.roomCode LIKE %?1%" +
            " AND reservations.hospitalRoom.roomName LIKE %?2% AND reservations.reservationStatus LIKE ?3" +
            " AND reservations.deleted = 0")
    Page<RoomReservations> findAllRoomReservations(String roomCode, String roomName, Integer status, Pageable pageable);
}