package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.Appointment;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}