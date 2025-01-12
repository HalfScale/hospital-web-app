package com.muffin.repository;

import com.muffin.model.DoctorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorCodeRepository extends JpaRepository<DoctorCode, Long> {
    Optional<DoctorCode> findByDoctorCode(String doctorCode);
}
