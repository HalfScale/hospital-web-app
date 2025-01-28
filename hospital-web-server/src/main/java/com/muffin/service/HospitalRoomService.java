package com.muffin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffin.exception.HospitalException;
import com.muffin.filehandler.FileManager;
import com.muffin.model.HospitalRooms;
import com.muffin.model.RoomReservations;
import com.muffin.model.UserDetails;
import com.muffin.model.dto.HospitalRoomRequest;
import com.muffin.model.dto.HospitalRoomResponse;
import com.muffin.repository.RoomReservationsRepository;
import com.muffin.repository.UserDetailsRepository;
import com.muffin.utility.AuthUtil;
import com.muffin.utility.Constants;
import com.muffin.utility.SystemUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.muffin.repository.HospitalRoomRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Transactional(rollbackOn = {Exception.class})
@Service
@Slf4j
@RequiredArgsConstructor
public class HospitalRoomService {

    private final HospitalRoomRepository hospitalRoomRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final AuthUtil authUtil;
    private final RoomReservationsRepository roomReservationsRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final FileManager fileManager;

    public ResponseEntity<Object> findById(String id) {
        HospitalRooms hospitalRooms = hospitalRoomRepository.findByIdAndDeletedFalse(Long.valueOf(id))
                .orElseThrow(() -> new HospitalException("Hospital room not existing!"));

        HospitalRoomResponse hospitalRoomResponse = modelMapper.map(hospitalRooms, HospitalRoomResponse.class);

        Long creatorId = hospitalRooms.getCreatedBy();
        Long updaterId = hospitalRooms.getUpdatedBy();
        UserDetails creator = userDetailsRepository.findByUsersId(creatorId).orElseThrow(() -> new HospitalException("User not existing!"));
        UserDetails updater = userDetailsRepository.findByUsersId(updaterId).orElseThrow(() -> new HospitalException("User not existing!"));

        hospitalRoomResponse.setCreatedBy(String.format("%s %s", creator.getFirstName(), creator.getLastName()));
        hospitalRoomResponse.setUpdatedBy(String.format("%s %s", updater.getFirstName(), updater.getLastName()));

        return ResponseEntity.ok(hospitalRoomResponse);
    }

    public ResponseEntity<Object> findAll(String code, String name, Pageable pageable) {
        code = !Objects.isNull(code) || StringUtils.hasText(code) ? code.trim() : "";
        name = !Objects.isNull(name) || StringUtils.hasText(name) ? name.trim() : "";

        Page<HospitalRoomResponse> hospitalRoomResponses = hospitalRoomRepository
                .findAllRoomByCodeOrName(code, name, pageable).map(hospitalRoom -> {

                    Long creatorId = hospitalRoom.getCreatedBy();
                    Long updaterId = hospitalRoom.getUpdatedBy();
                    UserDetails creator = userDetailsRepository.findByUsersId(creatorId).orElseThrow(() -> new HospitalException("User not existing!"));
                    UserDetails updater = userDetailsRepository.findByUsersId(updaterId).orElseThrow(() -> new HospitalException("User not existing!"));

                    HospitalRoomResponse hospitalRoomResponse = new HospitalRoomResponse();
                    hospitalRoomResponse.setId(hospitalRoom.getId());
                    hospitalRoomResponse.setRoomCode(hospitalRoom.getRoomCode());
                    hospitalRoomResponse.setRoomName(hospitalRoom.getRoomName());
                    hospitalRoomResponse.setRoomImage(hospitalRoom.getRoomImage());
                    hospitalRoomResponse.setDescription(hospitalRoomResponse.getDescription());
                    hospitalRoomResponse.setCreatedBy(String.format("%s %s", creator.getFirstName(), creator.getLastName()));
                    hospitalRoomResponse.setUpdatedBy(String.format("%s %s", updater.getFirstName(), updater.getLastName()));

                    return hospitalRoomResponse;
                });
        return ResponseEntity.ok(SystemUtil.mapToGenericPageableResponse(hospitalRoomResponses));
    }

    public ResponseEntity<Object> addHospitalRoom(String hospitalRoomRequestDto, MultipartFile image) throws JsonProcessingException {
        Long currentUserId = authUtil.getCurrentUser().getId();

        HospitalRoomRequest hospitalRoomRequest = objectMapper.readValue(hospitalRoomRequestDto, HospitalRoomRequest.class);
        HospitalRooms savedHospitalRooms = hospitalRoomRepository.save(this.mapToEntity(hospitalRoomRequest, currentUserId));

        this.setHospitalRoomImage(savedHospitalRooms, image);

        return ResponseEntity.ok(hospitalRoomRepository.save(savedHospitalRooms).getId());
    }

    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<Object> updateHospitalRoom(String hospitalRoomRequestDto, MultipartFile image) throws JsonProcessingException {
        Long currentUserId = authUtil.getCurrentUser().getId();

        HospitalRoomRequest hospitalRoomRequest = objectMapper.readValue(hospitalRoomRequestDto, HospitalRoomRequest.class);

        HospitalRooms hospitalRooms = hospitalRoomRepository.findByIdAndDeletedFalse(hospitalRoomRequest.getId())
                .orElseThrow(() -> new HospitalException("Hospital room not existing!"));
        String hospitalRoomImage = hospitalRooms.getRoomImage();
        hospitalRooms.setUpdatedBy(currentUserId);
        hospitalRooms.setModified(ZonedDateTime.now());
        modelMapper.map(hospitalRoomRequest, hospitalRooms);
        hospitalRooms.setRoomImage(hospitalRoomImage);
        this.setHospitalRoomImage(hospitalRooms, image);

        List<RoomReservations> associatedRoomReservations = roomReservationsRepository
                .findAllByHospitalRoomId(hospitalRooms.getId());

        // update all reservations that is associated to this hospital room during update
        if(!associatedRoomReservations.isEmpty()) {
            log.info("associatedRoomReservations: [{}]", associatedRoomReservations.size());
            associatedRoomReservations.forEach(reservation -> {
                reservation.setRoomCode(hospitalRooms.getRoomCode());
                roomReservationsRepository.save(reservation);
            });
        }

        return ResponseEntity.ok(hospitalRoomRepository.save(hospitalRooms).getId());
    }

    public ResponseEntity<Object> deleteHospitalRoom(String id) {
        Long currentUserId = authUtil.getCurrentUser().getId();

        HospitalRooms hospitalRooms = hospitalRoomRepository.findByIdAndDeletedFalse(Long.valueOf(id))
                .orElseThrow(() -> new HospitalException("Hospital room doesn't exist!"));

        hospitalRooms.setUpdatedBy(currentUserId);
        hospitalRooms.setModified(ZonedDateTime.now());
        hospitalRooms.setDeleted(true);
        hospitalRooms.setDeletedDate(ZonedDateTime.now());

        hospitalRoomRepository.save(hospitalRooms);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> validateHospitalRoom(Long roomId, String roomCode, String roomName) {
        List<HospitalRooms> hospitalRooms = null;

        if(Objects.isNull(roomId)) {
            hospitalRooms = hospitalRoomRepository.findByRoomCodeOrRoomName(roomCode, roomName)
                    .orElse(null);
        }else {
            hospitalRooms = hospitalRoomRepository
                    .findAllRoomByCodeOrNameAndId(roomId, roomCode, roomName).orElse(null);
        }

        if (!Objects.isNull(hospitalRooms) && !hospitalRooms.isEmpty()) {
            throw new HospitalException("Hospital Room is Existing!");
        }

        return ResponseEntity.noContent().build();
    }

    private void setHospitalRoomImage(HospitalRooms hospitalRooms, MultipartFile image) {
        if (!Objects.isNull(image) && !image.isEmpty()) {

            if (!Objects.isNull(hospitalRooms.getRoomImage())) {
                fileManager.setProperties(hospitalRooms.getRoomImage(), Constants.IMAGE_IDENTIFIER_HOSPITAL_ROOM, image);
                fileManager.delete(); // delete previous hospital image
            }

            fileManager.setProperties(image.getOriginalFilename(), Constants.IMAGE_IDENTIFIER_HOSPITAL_ROOM, image);
            String encryptedFileName = fileManager.upload();
            hospitalRooms.setRoomImage(encryptedFileName);
        }
    }

    private HospitalRooms mapToEntity(HospitalRoomRequest hospitalRoomRequest, Long currentUserId) {
        Long requestId = hospitalRoomRequest.getId();
        HospitalRooms hospitalRooms = new HospitalRooms();
        hospitalRooms.setId(hospitalRoomRequest.getId());
        hospitalRooms.setRoomCode(hospitalRoomRequest.getRoomCode());
        hospitalRooms.setRoomName(hospitalRoomRequest.getRoomName());
        hospitalRooms.setDescription(hospitalRoomRequest.getDescription());
        hospitalRooms.setModified(ZonedDateTime.now());

        if (Objects.isNull(requestId) || requestId == Constants.NEW_ENTITY_ID) {
            hospitalRooms.setCreated(ZonedDateTime.now());
            hospitalRooms.setCreatedBy(currentUserId);
        }

        hospitalRooms.setUpdatedBy(currentUserId);

        return hospitalRooms;
    }
}