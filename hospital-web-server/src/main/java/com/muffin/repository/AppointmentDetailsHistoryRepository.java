package com.muffin.repository;

import com.muffin.model.AppointmentDetailsHistory;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AppointmentDetailsHistoryRepository extends JpaRepository<AppointmentDetailsHistory, Long> {

}