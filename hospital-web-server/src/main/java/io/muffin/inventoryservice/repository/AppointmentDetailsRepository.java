package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.AppointmentDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentDetailsRepository extends JpaRepository<AppointmentDetails, Long> {

    Optional<AppointmentDetails> findByAppointmentId(Long id);

    @Query("SELECT appointmentDetails FROM AppointmentDetails appointmentDetails WHERE (?1 < appointmentDetails.endDate AND ?2 > appointmentDetails.startDate)" +
            " AND appointmentDetails.appointment.doctor.users.id LIKE ?3 AND appointmentDetails.appointment.appointmentStatus IN (1, 3, 4)")
    Page<AppointmentDetails> findDoctorAppointments(LocalDateTime startDate, LocalDateTime endDate, Long doctorId, Pageable pageable);

    @Query("SELECT ad FROM AppointmentDetails ad WHERE ad.appointment.patient.users.id = ?1 " +
            "AND (ad.appointment.id = ?2 " +
            "OR CONCAT(ad.appointment.doctor.firstName, ad.appointment.doctor.lastName) LIKE %?3%)")
    Page<AppointmentDetails> findAllAppointmentsByDoctor(Long currentUserId, Long appointmentId, String username, Pageable pageable);

    @Query("SELECT ad FROM AppointmentDetails ad WHERE ad.appointment.id = ?1 " +
            "AND ad.appointment.doctor.users.id = ?2 " +
            "AND CONCAT(ad.appointment.patient.firstName, ad.appointment.patient.lastName) LIKE %?3%")
    Page<AppointmentDetails> findAllAppointmentsByPatient(Long appointmentId, Long currentUserId, String username, Pageable pageable);
}
