package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.HospitalRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface HospitalRoomRepository extends JpaRepository<HospitalRoom, Long> {

    Optional<HospitalRoom> findByIdAndDeletedFalse(Long id);

    @Query("SELECT room FROM HospitalRoom room WHERE (room.roomCode LIKE %?1% "
            + "AND room.roomName LIKE %?2%) AND room.deleted = 0")
    Page<HospitalRoom> findAllRoomByCodeOrName(String code, String name, Pageable pageable);
}