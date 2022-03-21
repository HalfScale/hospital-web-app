package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.DoctorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorCodeRepository extends JpaRepository<DoctorCode, Long> {
    Optional<DoctorCode> findByDoctorCode(String doctorCode);
}
