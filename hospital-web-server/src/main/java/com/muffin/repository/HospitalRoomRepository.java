package com.muffin.repository;

import com.muffin.model.HospitalRooms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRoomRepository extends JpaRepository<HospitalRooms, Long> {

    Optional<HospitalRooms> findByIdAndDeletedFalse(Long id);
    Optional<List<HospitalRooms>> findByRoomCodeOrRoomName(String code, String name);

    @Query("SELECT room FROM HospitalRooms room WHERE (room.roomCode LIKE %?1% "
            + "AND room.roomName LIKE %?2%) AND room.deleted = 0")
    Page<HospitalRooms> findAllRoomByCodeOrName(String code, String name, Pageable pageable);

    @Query("SELECT room FROM HospitalRooms room WHERE room.id != ?1 AND (room.roomCode LIKE ?2 "
            + "OR room.roomName LIKE ?3) AND room.deleted = 0")
    Optional<List<HospitalRooms>> findAllRoomByCodeOrNameAndId(long id, String code, String name);
}