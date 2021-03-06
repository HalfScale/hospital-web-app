package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.exception.AuthenticationException;
import io.muffin.inventoryservice.exception.HospitalException;
import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.HospitalRoom;
import io.muffin.inventoryservice.model.RoomReservations;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.HospitalRoomResponse;
import io.muffin.inventoryservice.model.dto.ReservationRequest;
import io.muffin.inventoryservice.model.dto.ReservationResponse;
import io.muffin.inventoryservice.repository.HospitalRoomRepository;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import io.muffin.inventoryservice.utility.SystemUtil;
import org.apache.tomcat.jni.Local;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.muffin.inventoryservice.repository.RoomReservationsRepository;

import javax.xml.ws.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomReservationsService {

    private final RoomReservationsRepository roomReservationsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final HospitalRoomRepository hospitalRoomRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;

    public ResponseEntity<Object> findById(String id) {
        RoomReservations roomReservations = roomReservationsRepository.findByIdNotDeleted(Long.valueOf(id))
                .orElseThrow(() -> new HospitalException("Room reservation not found!"));

        HospitalRoom hospitalRoom = roomReservations.getHospitalRoom();
        HospitalRoomResponse hospitalRoomResponse = modelMapper.map(hospitalRoom, HospitalRoomResponse.class);

        UserDetails reservedByUser = userDetailsRepository.findByUsersId(roomReservations.getReservedByUserId())
                .orElseThrow(() -> new HospitalException("User not existing!"));
        UserDetails updatedByUser = userDetailsRepository.findByUsersId(roomReservations.getUpdatedBy())
                .orElseThrow(() -> new HospitalException("User not existing!"));

        ReservationResponse reservationResponse = this.mapToReservationResponse(roomReservations);
        reservationResponse.setReservedById(reservedByUser.getId());
        reservationResponse.setHospitalRoomResponse(hospitalRoomResponse);
        reservationResponse.setReservedByUsername(String.format("%s %s", reservedByUser.getFirstName(), reservedByUser.getLastName()));
        reservationResponse.setUpdatedBy(String.format("%s %s", updatedByUser.getFirstName(), updatedByUser.getLastName()));
        return ResponseEntity.ok(reservationResponse);
    }

    public ResponseEntity<Object> findAll(String roomCode, String roomName, String status, Pageable pageable) {
        roomCode = Objects.isNull(roomCode) ? "" : roomCode;
        roomName = Objects.isNull(roomName) ? "" : roomName;

        String statusResult = Constants.ALL_RESERVATION_STATUS_AS_STRING.stream()
                .filter(reservationStatus -> reservationStatus.equals(status))
                .findFirst().orElseThrow(() -> new HospitalException("Unknown reservation status!"));

        if (statusResult.equals(String.valueOf(Constants.RESERVATION_ALL))) {
            statusResult = "";
        }
        log.info("status result {}", statusResult);
        Page<ReservationResponse> reservationResponses = roomReservationsRepository
                .findAllRoomReservations(roomCode, roomName, statusResult, pageable)
                .map(roomReservations -> {
                    UserDetails reservedByUser = userDetailsRepository.findByUsersId(roomReservations.getReservedByUserId())
                            .orElseThrow(() -> new HospitalException("User not existing!"));
                    UserDetails updatedByUser = userDetailsRepository.findByUsersId(roomReservations.getUpdatedBy())
                            .orElseThrow(() -> new HospitalException("User not existing!"));
                    HospitalRoom hospitalRoom = roomReservations.getHospitalRoom();
                    HospitalRoomResponse hospitalRoomResponse = modelMapper.map(hospitalRoom, HospitalRoomResponse.class);

                    ReservationResponse reservationResponse = this.mapToReservationResponse(roomReservations);
                    reservationResponse.setReservedById(reservedByUser.getId());
                    reservationResponse.setHospitalRoomResponse(hospitalRoomResponse);
                    reservationResponse.setReservedByUsername(String.format("%s %s", reservedByUser.getFirstName(), reservedByUser.getLastName()));
                    reservationResponse.setUpdatedBy(String.format("%s %s", updatedByUser.getFirstName(), updatedByUser.getLastName()));

                    return reservationResponse;
                });

        return ResponseEntity.ok(SystemUtil.mapToGenericPageableResponse(reservationResponses));
    }

    public ResponseEntity<Object> createRoomReservation(ReservationRequest reservationRequest) throws JsonProcessingException {
        RoomReservations roomReservations = new RoomReservations();
        this.mapToRoomReservation(roomReservations, reservationRequest);
        roomReservationsRepository.save(roomReservations);
        return ResponseEntity.ok(roomReservations.getId());
    }


    public ResponseEntity<Object> updateRoomReservation(ReservationRequest reservationRequest) throws JsonProcessingException {
        JwtUserDetails currentUser = authUtil.getCurrentUser();
        String reservationId = reservationRequest.getId();
        RoomReservations roomReservations = roomReservationsRepository.findById(Long.valueOf(reservationId))
                .orElseThrow(() -> new HospitalException("Room reservation doesn't exist!"));
        roomReservations.setHasAssociatedAppointmentId(reservationRequest.isHasAssociatedAppointmentId());

        if(currentUser.getId() != roomReservations.getReservedByUserId()) {
            throw new AuthenticationException("Unauthorized user!");
        }

        if (reservationRequest.isHasAssociatedAppointmentId()) {
            roomReservations.setAssociatedAppointmentId(Long.valueOf(reservationRequest.getAssociatedAppointmentId()));
        }

        roomReservations.setStartDate(reservationRequest.getStartDate());
        roomReservations.setEndDate(reservationRequest.getEndDate());
        roomReservations.setUpdatedBy(currentUser.getId());
        roomReservations.setModified(LocalDateTime.now());

        roomReservationsRepository.save(roomReservations);

        return ResponseEntity.ok(roomReservations.getId());
    }

    public ResponseEntity<Object> updateRoomReservationStatus(String reservationId, String status) {
        JwtUserDetails currentUser = authUtil.getCurrentUser();
        RoomReservations roomReservations = roomReservationsRepository.findById(Long.valueOf(reservationId))
                .orElseThrow(() -> new HospitalException("Room reservation not found!"));

        if(currentUser.getId() != roomReservations.getReservedByUserId()) {
            throw new AuthenticationException("Unauthorized user!");
        }

        roomReservations.setReservationStatus(status);
        roomReservations.setModified(LocalDateTime.now());
        roomReservations.setUpdatedBy(currentUser.getId());

        roomReservationsRepository.save(roomReservations);

        return ResponseEntity.ok(roomReservations.getId());
    }

    public ResponseEntity<Object> deleteRoomReservation(String reservationId) {
        JwtUserDetails currentUser = authUtil.getCurrentUser();

        RoomReservations roomReservations = roomReservationsRepository.findById(Long.valueOf(reservationId))
                .orElseThrow(() -> new HospitalException("Room reservation not found!"));

        if(currentUser.getId() != roomReservations.getReservedByUserId()) {
            throw new AuthenticationException("Unauthorized user!");
        }

        roomReservations.setDeleted(true);
        roomReservations.setDeletedDate(LocalDateTime.now());
        roomReservations.setModified(LocalDateTime.now());
        roomReservations.setUpdatedBy(currentUser.getId());

        roomReservationsRepository.save(roomReservations);

        return ResponseEntity.ok(roomReservations.getId());
    }

    public ResponseEntity<Object> checkOverLappingReservations(Map<String, String> reservationRequest, Pageable pageable) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Page<ReservationResponse> reservationResponses = roomReservationsRepository
                .findOverlappingReservations(LocalDateTime.parse(reservationRequest.get("startDate"), dateTimeFormatter),
                        LocalDateTime.parse(reservationRequest.get("endDate"), dateTimeFormatter),
                        reservationRequest.get("roomCode"), pageable).map(roomReservations -> {

                    UserDetails reservedByUser = userDetailsRepository.findByUsersId(roomReservations.getReservedByUserId())
                            .orElseThrow(() -> new HospitalException("User not existing!"));
                    UserDetails updatedByUser = userDetailsRepository.findByUsersId(roomReservations.getUpdatedBy())
                            .orElseThrow(() -> new HospitalException("User not existing!"));
                    HospitalRoom hospitalRoom = roomReservations.getHospitalRoom();
                    HospitalRoomResponse hospitalRoomResponse = modelMapper.map(hospitalRoom, HospitalRoomResponse.class);

                    ReservationResponse reservationResponse = this.mapToReservationResponse(roomReservations);
                    reservationResponse.setReservedById(reservedByUser.getId());
                    reservationResponse.setHospitalRoomResponse(hospitalRoomResponse);
                    reservationResponse.setReservedByUsername(String.format("%s %s", reservedByUser.getFirstName(), reservedByUser.getLastName()));
                    reservationResponse.setUpdatedBy(String.format("%s %s", updatedByUser.getFirstName(), updatedByUser.getLastName()));

                    return reservationResponse;
                });
        return ResponseEntity.ok(SystemUtil.mapToGenericPageableResponse(reservationResponses));
    }

    private ReservationResponse mapToReservationResponse(RoomReservations roomReservations) {
        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setId(roomReservations.getId());
        reservationResponse.setHasAssociatedAppointmentId(roomReservations.isHasAssociatedAppointmentId());
        reservationResponse.setAssociatedAppointmentId(roomReservations.getAssociatedAppointmentId());
        reservationResponse.setReservationStatus(Integer.valueOf(roomReservations.getReservationStatus()));
        reservationResponse.setStartDate(roomReservations.getStartDate());
        reservationResponse.setEndDate(roomReservations.getEndDate());
        return reservationResponse;
    }

    private void mapToRoomReservation(RoomReservations roomReservations, ReservationRequest reservationRequest) {
        JwtUserDetails currentUser = authUtil.getCurrentUser();
        String hospitalRoomId = reservationRequest.getHospitalRoomId();
        HospitalRoom hospitalRoom = hospitalRoomRepository.findById(Long.valueOf(hospitalRoomId))
                .orElseThrow(() -> new HospitalException("Hospital room not existing!"));

        roomReservations.setHospitalRoom(hospitalRoom);
        roomReservations.setRoomCode(hospitalRoom.getRoomCode());
        roomReservations.setHasAssociatedAppointmentId(reservationRequest.isHasAssociatedAppointmentId());
        if (reservationRequest.isHasAssociatedAppointmentId()) {
            roomReservations.setAssociatedAppointmentId(Long.valueOf(reservationRequest.getAssociatedAppointmentId()));
        }
        roomReservations.setReservedByUserId(currentUser.getId());
        roomReservations.setStartDate(reservationRequest.getStartDate());
        roomReservations.setEndDate(reservationRequest.getEndDate());
        roomReservations.setReservationStatus(String.valueOf(Constants.RESERVATION_CREATED));
        roomReservations.setUpdatedBy(currentUser.getId());
        roomReservations.setCreated(LocalDateTime.now());
        roomReservations.setModified(LocalDateTime.now());
        roomReservations.setDeleted(false);
    }
}
