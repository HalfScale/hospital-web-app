package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.AppointmentDetails;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AppointmentDetailsRepository extends JpaRepository<AppointmentDetails, Long> {

    Optional<AppointmentDetails> findByAppointmentId(Long id);
}