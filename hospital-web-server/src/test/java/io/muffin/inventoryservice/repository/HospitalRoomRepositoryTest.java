package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.HospitalRoom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class HospitalRoomRepositoryTest {

    @Autowired
    HospitalRoomRepository hospitalRoomRepository;


    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void test_findByIdAndDeletedFalse() {
        Optional<HospitalRoom> optionalHospitalRoom = hospitalRoomRepository
                .findByIdAndDeletedFalse(createHospitalRoom().getId());

        assertTrue(optionalHospitalRoom.isPresent());
    }

    @Test
    public void test_findByRoomCodeOrRoomName(){
        HospitalRoom hospitalRoom = createHospitalRoom();
        createHospitalRoom();
        Optional<List<HospitalRoom>> optionalHospitalRooms = hospitalRoomRepository.
                findByRoomCodeOrRoomName(hospitalRoom.getRoomCode(), hospitalRoom.getRoomName());

        assertTrue(optionalHospitalRooms.isPresent());
        assertFalse(optionalHospitalRooms.get().isEmpty());
    }

    @Test
    public void test_findAllRoomByCodeOrName(){
        HospitalRoom hospitalRoom = createHospitalRoom();
        createHospitalRoom();

        Page<HospitalRoom> pagedHospitalRooms = hospitalRoomRepository
                .findAllRoomByCodeOrName(hospitalRoom.getRoomCode(), hospitalRoom.getRoomName(), Pageable.unpaged());

        assertFalse(pagedHospitalRooms.isEmpty());
    }

    @Test
    public void test_findAllRoomByCodeOrNameAndId(){
        createHospitalRoom();
        createHospitalRoom();
        HospitalRoom hospitalRoom = createHospitalRoom();
        Optional<List<HospitalRoom>> optionalHospitalRooms = hospitalRoomRepository.
                findAllRoomByCodeOrNameAndId(hospitalRoom.getId(), hospitalRoom.getRoomCode(), hospitalRoom.getRoomName());

        assertTrue(optionalHospitalRooms.isPresent());
        assertFalse(optionalHospitalRooms.get().isEmpty());

    }

    public HospitalRoom createHospitalRoom(){
        HospitalRoom hospitalRoom = HospitalRoom.builder()
                .roomCode("101")
                .roomName("Test room")
                .description("Test description")
                .createdBy(1L)
                .updatedBy(1L)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .deleted(false)
                .build();

        HospitalRoom savedHospitalRoom = hospitalRoomRepository.save(hospitalRoom);
        testEntityManager.flush();
        return savedHospitalRoom;
    }
}
