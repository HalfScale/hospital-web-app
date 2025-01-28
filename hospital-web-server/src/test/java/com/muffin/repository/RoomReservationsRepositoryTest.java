package com.muffin.repository;

import com.muffin.model.HospitalRooms;
import com.muffin.model.RoomReservations;
import com.muffin.utility.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class RoomReservationsRepositoryTest {

    @Autowired
    RoomReservationsRepository roomReservationsRepository;

    @Autowired
    HospitalRoomRepository hospitalRoomRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void test_findAllByHospitalRoomId(){
        RoomReservations roomReservation = addReservation();
        List<RoomReservations> roomReservations = roomReservationsRepository
                .findAllByHospitalRoomId(roomReservation.getHospitalRooms().getId());

        assertFalse(roomReservations.isEmpty());
    }

    @Test
    public void test_findAllByHospitalRoomId_multipleReservations(){
        RoomReservations roomReservation1 = addReservation(1L);
        addReservation(roomReservation1.getHospitalRooms().getId());

        List<RoomReservations> roomReservations = roomReservationsRepository.findAllByHospitalRoomId(roomReservation1.getHospitalRooms().getId());
        assertTrue(roomReservations.size() > 1);
    }

    @Test
    public void test_findByIdNotDeleted(){
        RoomReservations roomReservations = addReservation();
        Optional<RoomReservations> roomReservationsOptional = roomReservationsRepository.findByIdNotDeleted(roomReservations.getId());

        Assertions.assertTrue(roomReservationsOptional.isPresent());
        Assertions.assertNotNull(roomReservationsOptional.get());
    }

    @Test
    public void test_findAllRoomReservations(){
        addReservation();

        String roomCode = "101";
        String roomName = "101 test room";
        String roomStatus = String.valueOf(Constants.RESERVATION_CREATED);
        Pageable pageable = Pageable.unpaged();


        Page<RoomReservations> roomReservations = roomReservationsRepository.findAllRoomReservations(roomCode,
                roomName, roomStatus, pageable);

        assertFalse(roomReservations.isEmpty());
    }

    @Test
    public void test_findOverlappingReservations(){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime startDate1 = ZonedDateTime.of(2024, 4, 21, 10, 30, 0, 0, zoneId);
        ZonedDateTime endDate1 = ZonedDateTime.of(2024, 4, 21, 11, 45, 0, 0, zoneId);
        ZonedDateTime startDate2 = ZonedDateTime.of(2024, 4, 21, 11, 30, 0, 0, zoneId);
        ZonedDateTime endDate2 = ZonedDateTime.of(2024, 4, 21, 12, 30, 0, 0, zoneId);

        addReservation(startDate1, endDate1);

        Page<RoomReservations> overlappingReservations = roomReservationsRepository.findOverlappingReservations(startDate2, endDate2, "101", Pageable.unpaged());

        assertFalse(overlappingReservations.getContent().isEmpty());
    }

    private RoomReservations addReservation(Long hospitalRoomId, ZonedDateTime startDate, ZonedDateTime endDate){
        if (startDate == null) {
            startDate = ZonedDateTime.now();
        }

        if (endDate == null) {
            endDate = ZonedDateTime.now();
        }

        HospitalRooms hospitalRooms = HospitalRooms.builder()
                .id(hospitalRoomId)
                .roomCode("101")
                .roomName("101 test room")
                .description("test description")
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .createdBy(1L)
                .updatedBy(1L)
                .deleted(false)
                .build();

        HospitalRooms persistedHospitalRooms = hospitalRoomRepository.save(hospitalRooms);

        RoomReservations roomReservation = RoomReservations.builder()
                .hospitalRooms(persistedHospitalRooms)
                .roomCode("101")
                .reservedByUserId(1L)
                .hasAssociatedAppointmentId(false)
                .reservationStatus(String.valueOf(Constants.RESERVATION_CREATED))
                .startDate(startDate)
                .endDate(endDate)
                .updatedBy(1L)
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .deleted(false)
                .build();

        RoomReservations persistedRoomReservation = roomReservationsRepository.save(roomReservation);
        testEntityManager.flush();
        return persistedRoomReservation;
    }

    private RoomReservations addReservation(){
        return addReservation(null, null, null);
    }

    private RoomReservations addReservation(Long id){
        return addReservation(id, null, null);
    }

    private void addReservation(ZonedDateTime starDate, ZonedDateTime endDate){
        addReservation(null, starDate, endDate);
    }

}
