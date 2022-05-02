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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HospitalRoomService {

    private final HospitalRoomRepository hospitalRoomRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final AuthUtil authUtil;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    public ResponseEntity<Object> findById(String id) {
        HospitalRoom hospitalRoom = hospitalRoomRepository.findById(Long.valueOf(id))
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

    public ResponseEntity<Object> findAll(Pageable pageable) {
        Page<HospitalRoomResponse> hospitalRoomResponses = hospitalRoomRepository.findAll(pageable).map(hospitalRoom -> {
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

        // assign values to the entity
        HospitalRoomRequest hospitalRoomRequest = objectMapper.readValue(hospitalRoomRequestDto, HospitalRoomRequest.class);
        HospitalRoom savedHospitalRoom = hospitalRoomRepository.save(this.mapToEntity(hospitalRoomRequest, currentUserId));

        if (!Objects.isNull(image)) {
            fileService.setEntityId(savedHospitalRoom.getId());
            fileService.setIdentifier(fileService.getIdentifierPath(Constants.IMAGE_IDENTIFIER_HOSPITAL_ROOM));
            fileService.setFile(image);
            String hashedFile = fileService.uploadToLocalFileSystem();
            log.info("HASHED_FILE => [{}]", hashedFile);

            savedHospitalRoom.setRoomImage(hashedFile);
        }

        return ResponseEntity.ok(hospitalRoomRepository.save(savedHospitalRoom).getId());
    }

    private HospitalRoom mapToEntity(HospitalRoomRequest hospitalRoomRequest, Long currentUserId) {
        Long requestId = hospitalRoomRequest.getId();
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setId(hospitalRoomRequest.getId());
        hospitalRoom.setRoomCode(hospitalRoomRequest.getRoomCode());
        hospitalRoom.setRoomName(hospitalRoomRequest.getRoomName());
        hospitalRoom.setDescription(hospitalRoomRequest.getDescription());
        hospitalRoom.setModified(LocalDateTime.now());

        if(Objects.isNull(requestId) || requestId == Constants.NEW_ENTITY_ID) {
            hospitalRoom.setCreated(LocalDateTime.now());
            hospitalRoom.setCreatedBy(currentUserId);
        }

        hospitalRoom.setUpdatedBy(currentUserId);

        return hospitalRoom;
    }
}