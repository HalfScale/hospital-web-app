package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.RoomReservations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomReservationsRepository extends JpaRepository<RoomReservations, Long> {

    @Query("SELECT reservations FROM RoomReservations reservations WHERE " +
            "reservations.hospitalRoom.id = ?1 AND reservations.deleted = 0")
    List<RoomReservations> findAllByHospitalRoomId(long id);

    @Query("SELECT reservations FROM RoomReservations reservations WHERE reservations.id = ?1 AND " +
            "reservations.deleted = 0")
    Optional<RoomReservations> findByIdNotDeleted(long id);

    @Query("SELECT reservations FROM RoomReservations reservations WHERE reservations.roomCode LIKE %?1%" +
            " AND reservations.hospitalRoom.roomName LIKE %?2% AND reservations.reservationStatus LIKE %?3%" +
            " AND reservations.deleted = 0")
    Page<RoomReservations> findAllRoomReservations(String roomCode, String roomName, String status, Pageable pageable);

    @Query("SELECT reservations FROM RoomReservations reservations WHERE (?1 < reservations.endDate AND ?2 > reservations.startDate)" +
            " AND reservations.roomCode LIKE ?3 AND reservations.deleted = 0 AND reservations.reservationStatus != 1")
    Page<RoomReservations> findOverlappingReservations(LocalDateTime startDate, LocalDateTime endDate, String roomCode, Pageable pageable);



}