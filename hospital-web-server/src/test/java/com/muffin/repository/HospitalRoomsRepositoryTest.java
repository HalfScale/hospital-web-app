package com.muffin.repository;

import com.muffin.model.HospitalRooms;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class HospitalRoomsRepositoryTest {

    @Autowired
    HospitalRoomRepository hospitalRoomRepository;


    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void test_findByIdAndDeletedFalse() {
        Optional<HospitalRooms> optionalHospitalRoom = hospitalRoomRepository
                .findByIdAndDeletedFalse(createHospitalRoom().getId());

        assertTrue(optionalHospitalRoom.isPresent());
    }

    @Test
    public void test_findByRoomCodeOrRoomName(){
        HospitalRooms hospitalRooms = createHospitalRoom();
        createHospitalRoom();
        Optional<List<HospitalRooms>> optionalHospitalRooms = hospitalRoomRepository.
                findByRoomCodeOrRoomName(hospitalRooms.getRoomCode(), hospitalRooms.getRoomName());

        assertTrue(optionalHospitalRooms.isPresent());
        assertFalse(optionalHospitalRooms.get().isEmpty());
    }

    @Test
    public void test_findAllRoomByCodeOrName(){
        HospitalRooms hospitalRooms = createHospitalRoom();
        createHospitalRoom();

        Page<HospitalRooms> pagedHospitalRooms = hospitalRoomRepository
                .findAllRoomByCodeOrName(hospitalRooms.getRoomCode(), hospitalRooms.getRoomName(), Pageable.unpaged());

        assertFalse(pagedHospitalRooms.isEmpty());
    }

    @Test
    public void test_findAllRoomByCodeOrNameAndId(){
        createHospitalRoom();
        createHospitalRoom();
        HospitalRooms hospitalRooms = createHospitalRoom();
        Optional<List<HospitalRooms>> optionalHospitalRooms = hospitalRoomRepository.
                findAllRoomByCodeOrNameAndId(hospitalRooms.getId(), hospitalRooms.getRoomCode(), hospitalRooms.getRoomName());

        assertTrue(optionalHospitalRooms.isPresent());
        assertFalse(optionalHospitalRooms.get().isEmpty());

    }

    public HospitalRooms createHospitalRoom(){
        HospitalRooms hospitalRooms = HospitalRooms.builder()
                .roomCode("101")
                .roomName("Test room")
                .description("Test description")
                .createdBy(1L)
                .updatedBy(1L)
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .deleted(false)
                .build();

        HospitalRooms savedHospitalRooms = hospitalRoomRepository.save(hospitalRooms);
        testEntityManager.flush();
        return savedHospitalRooms;
    }
}
