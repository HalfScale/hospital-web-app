package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.AppointmentHistory;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AppointmentHistoryRepository extends JpaRepository<AppointmentHistory, Long> {

}