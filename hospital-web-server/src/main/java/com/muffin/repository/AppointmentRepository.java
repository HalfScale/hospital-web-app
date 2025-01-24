package com.muffin.repository;

import com.muffin.model.Appointments;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, Long> {

}