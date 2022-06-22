package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.exception.HospitalException;
import io.muffin.inventoryservice.model.HospitalRoom;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.HospitalRoomRequest;
import io.muffin.inventoryservice.model.dto.HospitalRoomResponse;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import io.muffin.inventoryservice.utility.SystemUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.muffin.inventoryservice.repository.HospitalRoomRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final DeprecatedFileService deprecatedFileService;
    private final FileManager fileManager;

    public ResponseEntity<Object> findById(String id) {
        HospitalRoom hospitalRoom = hospitalRoomRepository.findByIdAndDeletedFalse(Long.valueOf(id))
                .orElseThrow(() -> new HospitalException("Hospital room not existing!"));

        HospitalRoomResponse hospitalRoomResponse = modelMapper.map(hospitalRoom, HospitalRoomResponse.class);

        Long creatorId = hospitalRoom.getCreatedBy();
        Long updaterId = hospitalRoom.getUpdatedBy();
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
        HospitalRoom savedHospitalRoom = hospitalRoomRepository.save(this.mapToEntity(hospitalRoomRequest, currentUserId));

        this.setHospitalRoomImage(savedHospitalRoom, image);

        return ResponseEntity.ok(hospitalRoomRepository.save(savedHospitalRoom).getId());
    }

    public ResponseEntity<Object> updateHospitalRoom(String hospitalRoomRequestDto, MultipartFile image) throws JsonProcessingException {
        Long currentUserId = authUtil.getCurrentUser().getId();

        HospitalRoomRequest hospitalRoomRequest = objectMapper.readValue(hospitalRoomRequestDto, HospitalRoomRequest.class);

        HospitalRoom hospitalRoom = hospitalRoomRepository.findByIdAndDeletedFalse(hospitalRoomRequest.getId())
                .orElseThrow(() -> new HospitalException("Hospital room not existing!"));
        String hospitalRoomImage = hospitalRoom.getRoomImage();
        hospitalRoom.setUpdatedBy(currentUserId);
        hospitalRoom.setModified(LocalDateTime.now());
        modelMapper.map(hospitalRoomRequest, hospitalRoom);
        hospitalRoom.setRoomImage(hospitalRoomImage);
        this.setHospitalRoomImage(hospitalRoom, image);

        return ResponseEntity.ok(hospitalRoomRepository.save(hospitalRoom).getId());
    }

    public ResponseEntity<Object> deleteHospitalRoom(String id) {
        Long currentUserId = authUtil.getCurrentUser().getId();

        HospitalRoom hospitalRoom = hospitalRoomRepository.findByIdAndDeletedFalse(Long.valueOf(id))
                .orElseThrow(() -> new HospitalException("Hospital room doesn't exist!"));

        hospitalRoom.setUpdatedBy(currentUserId);
        hospitalRoom.setModified(LocalDateTime.now());
        hospitalRoom.setDeleted(true);
        hospitalRoom.setDeletedDate(LocalDateTime.now());

        hospitalRoomRepository.save(hospitalRoom);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> validateHospitalRoom(String roomCode, String roomName) {
        List<HospitalRoom> hospitalRoom = hospitalRoomRepository.findByRoomCodeOrRoomName(roomCode, roomName).orElse(null);

        if (!Objects.isNull(hospitalRoom) && !hospitalRoom.isEmpty()) {
            throw new HospitalException("Hospital Room is Existing!");
        }

        return ResponseEntity.noContent().build();
    }

    private void setHospitalRoomImage(HospitalRoom hospitalRoom, MultipartFile image) {
        if (!Objects.isNull(image) && !image.isEmpty()) {

            if (!Objects.isNull(hospitalRoom.getRoomImage())) {
                fileManager.setProperties(hospitalRoom.getRoomImage(), Constants.IMAGE_IDENTIFIER_HOSPITAL_ROOM, image);
                fileManager.delete(); // delete previous hospital image
            }

            fileManager.setProperties(image.getOriginalFilename(), Constants.IMAGE_IDENTIFIER_HOSPITAL_ROOM, image);
            String encryptedFileName = fileManager.upload();
            hospitalRoom.setRoomImage(encryptedFileName);
        }
    }

    private HospitalRoom mapToEntity(HospitalRoomRequest hospitalRoomRequest, Long currentUserId) {
        Long requestId = hospitalRoomRequest.getId();
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setId(hospitalRoomRequest.getId());
        hospitalRoom.setRoomCode(hospitalRoomRequest.getRoomCode());
        hospitalRoom.setRoomName(hospitalRoomRequest.getRoomName());
        hospitalRoom.setDescription(hospitalRoomRequest.getDescription());
        hospitalRoom.setModified(LocalDateTime.now());

        if (Objects.isNull(requestId) || requestId == Constants.NEW_ENTITY_ID) {
            hospitalRoom.setCreated(LocalDateTime.now());
            hospitalRoom.setCreatedBy(currentUserId);
        }

        hospitalRoom.setUpdatedBy(currentUserId);

        return hospitalRoom;
    }
}