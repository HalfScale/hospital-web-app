package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.HospitalRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRoomRepository extends JpaRepository<HospitalRoom, Long> {

    Optional<HospitalRoom> findByIdAndDeletedFalse(Long id);
    Optional<List<HospitalRoom>> findByRoomCodeOrRoomName(String code, String name);

    @Query("SELECT room FROM HospitalRoom room WHERE (room.roomCode LIKE %?1% "
            + "AND room.roomName LIKE %?2%) AND room.deleted = 0")
    Page<HospitalRoom> findAllRoomByCodeOrName(String code, String name, Pageable pageable);

    @Query("SELECT room FROM HospitalRoom room WHERE room.id != ?1 AND (room.roomCode LIKE ?2 "
            + "OR room.roomName LIKE ?3) AND room.deleted = 0")
    Optional<List<HospitalRoom>> findAllRoomByCodeOrNameAndId(long id, String code, String name);
}