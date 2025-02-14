package com.muffin.service;

import com.muffin.model.DoctorCode;
import com.muffin.model.UserDetails;
import com.muffin.model.dto.DoctorCardResponse;
import com.muffin.model.dto.DoctorListResponse;
import com.muffin.model.dto.DoctorProfileResponse;
import com.muffin.repository.DoctorCodeRepository;
import com.muffin.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorCodeRepository doctorCodeRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<DoctorListResponse> findAllDoctor(String name, String doctorCode, Pageable pageable) {

        Page<DoctorCardResponse> doctorsPage = null;

        name = StringUtils.hasText(name) ? name : "";
        doctorCode = StringUtils.hasText(doctorCode) ? doctorCode : "";

        if (StringUtils.hasText(name) || StringUtils.hasText(doctorCode)) {
            log.info("GET_ALL_DOCTORS_WITH_PARAMS");
            String[] splitedName = name.split(" ");
            String firstName = splitedName[0];
            String lastName = splitedName[0];

            if (splitedName.length > 1) {
                lastName = splitedName[1];
                doctorsPage = mapToDoctorCardResponse(true,true, firstName, lastName, doctorCode, pageable);
            } else {
                doctorsPage = mapToDoctorCardResponse(false,true, firstName, lastName, doctorCode, pageable);
            }

        }

        if (!StringUtils.hasText(name) && !StringUtils.hasText(doctorCode)) {
            log.info("GET_ALL_DOCTORS");
            doctorsPage = mapToDoctorCardResponse(false, false, null, null, null, pageable);
        }


        DoctorListResponse doctorsResponse = new DoctorListResponse(doctorsPage.getContent(), doctorsPage.getNumber(),
                doctorsPage.getSize(), doctorsPage.getTotalElements(), doctorsPage.getTotalPages());
        return ResponseEntity.ok(doctorsResponse);
    }

    private Page<DoctorCardResponse> mapToDoctorCardResponse(boolean isFullName, boolean withDoctorParams, String firstName,
                                                             String lastName, String doctorCode, Pageable pageable) {

        if (withDoctorParams && isFullName) {
            return userDetailsRepository.findByFullName(firstName, lastName,
                            doctorCode, pageable)
                    .map(userDetails -> {
                        DoctorCode code = doctorCodeRepository.findTopByCodeOrderByCreatedDesc(userDetails.getDoctorCodeId()).orElse(null);
                        return new DoctorCardResponse(userDetails.getUsers().getId(), String.format("%s %s", userDetails.getFirstName(), userDetails.getLastName()),
                                userDetails.getProfileImage(), code.getSpecialization(), code.getDescription());
                    });
        }

        if (withDoctorParams && !isFullName) {
            return userDetailsRepository.findByName(firstName, doctorCode, pageable)
                    .map(userDetails -> {
                        DoctorCode code = doctorCodeRepository.findTopByCodeOrderByCreatedDesc(userDetails.getDoctorCodeId()).orElse(null);
                        return new DoctorCardResponse(userDetails.getUsers().getId(), String.format("%s %s", userDetails.getFirstName(), userDetails.getLastName()),
                                userDetails.getProfileImage(), code.getSpecialization(), code.getDescription());
                    });
        }

        return userDetailsRepository.findAllByDoctorCodeIdIsNotNull(pageable)
                .map(userDetails -> {
                    DoctorCode code = doctorCodeRepository.findTopByCodeOrderByCreatedDesc(userDetails.getDoctorCodeId()).orElse(null);
                    return new DoctorCardResponse(userDetails.getUsers().getId(), String.format("%s %s", userDetails.getFirstName(), userDetails.getLastName()),
                            userDetails.getProfileImage(), code.getSpecialization(), code.getDescription());
                });
    }

    public ResponseEntity<DoctorProfileResponse> findDoctorByUserId(String userId) {
        UserDetails userDetails = userDetailsRepository.findByUsersId(Long.valueOf(userId)).orElse(null);
        DoctorProfileResponse doctorProfileResponse = modelMapper.map(userDetails, DoctorProfileResponse.class);

        DoctorCode doctorCode = doctorCodeRepository.findTopByCodeOrderByCreatedDesc(userDetails.getDoctorCodeId())
                .orElseThrow(() -> new RuntimeException("Doctor code not found!"));
        doctorProfileResponse.setSpecialization(doctorCode.getSpecialization());
        return ResponseEntity.ok(doctorProfileResponse);
    }
}
