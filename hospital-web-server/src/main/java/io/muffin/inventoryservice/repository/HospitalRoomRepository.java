package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.HospitalRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface HospitalRoomRepository extends JpaRepository<HospitalRoom, Long> {

}