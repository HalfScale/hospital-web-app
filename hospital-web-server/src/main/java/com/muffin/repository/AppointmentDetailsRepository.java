package com.muffin.repository;

import com.muffin.model.AppointmentDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentDetailsRepository extends JpaRepository<AppointmentDetails, Long> {

    Optional<AppointmentDetails> findByAppointmentsId(Long id);

    @Query("SELECT appointmentDetails FROM AppointmentDetails appointmentDetails WHERE (?1 < appointmentDetails.endDate AND ?2 > appointmentDetails.startDate)" +
            " AND appointmentDetails.appointments.doctor.users.id LIKE ?3 AND appointmentDetails.appointments.appointmentStatus IN (1, 2)")
    Page<AppointmentDetails> findDoctorAppointments(LocalDateTime startDate, LocalDateTime endDate, Long doctorId, Pageable pageable);

    @Query("SELECT ad FROM AppointmentDetails ad WHERE (ad.appointments.patient.users.id = ?1 OR  ad.appointments.doctor.users.id = ?1) " +
            "AND ad.appointments.deleted = 0")
    Page<AppointmentDetails> findAllByCurrentUser(Long currentUserId, Pageable pageable);

    @Query("SELECT ad FROM AppointmentDetails ad WHERE (ad.appointments.patient.users.id = ?1 OR  ad.appointments.doctor.users.id = ?1) " +
            "AND ad.appointments.id = ?2 AND ad.appointments.deleted = 0")
    Page<AppointmentDetails> findAllByAppointmentId(Long currentUserId, Long appointmentId, Pageable pageable);

    @Query("SELECT ad FROM AppointmentDetails ad WHERE (ad.appointments.patient.users.id = ?1 OR  ad.appointments.doctor.users.id = ?1) " +
            "AND (CONCAT(ad.appointments.patient.firstName, ' ', ad.appointments.patient.lastName) LIKE %?2% " +
            "OR CONCAT(ad.appointments.doctor.firstName, ' ', ad.appointments.doctor.lastName) LIKE %?2%) AND ad.appointments.deleted = 0")
    Page<AppointmentDetails> findAllByName(Long currentUserId, String name, Pageable pageable);

    @Query("SELECT ad FROM AppointmentDetails ad WHERE (ad.appointments.patient.users.id = ?1 OR  ad.appointments.doctor.users.id = ?1) " +
            "AND (ad.appointments.id = ?2 " +
            "AND (CONCAT(ad.appointments.patient.firstName, ' ',ad.appointments.patient.lastName) LIKE %?3% " +
            "OR CONCAT(ad.appointments.doctor.firstName, ' ',ad.appointments.doctor.lastName) LIKE %?3%)) AND ad.appointments.deleted = 0")
    Page<AppointmentDetails> findAllByAppointmentIdAndName(Long currentUserId,  Long appointmentId, String name, Pageable pageable);
}
