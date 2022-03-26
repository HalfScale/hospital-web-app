package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.model.DoctorCode;
import io.muffin.inventoryservice.model.dto.DoctorCardResponse;
import io.muffin.inventoryservice.model.dto.DoctorListResponse;
import io.muffin.inventoryservice.repository.DoctorCodeRepository;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
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

        name = StringUtils.hasText(name) ? name : "";
        doctorCode = StringUtils.hasText(doctorCode) ? doctorCode : "";

        if (StringUtils.hasText(name) || StringUtils.hasText(doctorCode)) {
            String[] splitedName = name.split(" ");
            String firstName = splitedName[0];
            String lastName = "";

            if (splitedName.length > 1) {
                lastName = splitedName[1];
            }

            Page<DoctorCardResponse> doctorsPage = userDetailsRepository.findByFirstNameAndLastNameAndDoctorCode(firstName, lastName,
                            doctorCode, pageable)
                    .map(userDetails -> {
                        DoctorCode code = doctorCodeRepository.findByDoctorCode(userDetails.getDoctorCodeId()).orElse(null);
                        return new DoctorCardResponse(userDetails.getUsers().getId(),String.format("%s %s", userDetails.getFirstName(), userDetails.getLastName()),
                                userDetails.getProfileImage(), code.getSpecialization(), code.getDescription());
                    });

            DoctorListResponse doctorsResponse = new DoctorListResponse(doctorsPage.getContent(), doctorsPage.getNumber(),
                    doctorsPage.getSize(), doctorsPage.getTotalElements(), doctorsPage.getTotalPages());
            return ResponseEntity.ok(doctorsResponse);
        }

        Page<DoctorCardResponse> doctorsPage = userDetailsRepository.findAllByDoctorCodeIdIsNotNull(pageable)
                .map(userDetails -> {
            DoctorCode code = doctorCodeRepository.findByDoctorCode(userDetails.getDoctorCodeId()).orElse(null);
            return new DoctorCardResponse(userDetails.getUsers().getId(), String.format("%s %s", userDetails.getFirstName(), userDetails.getLastName()),
                    userDetails.getProfileImage(), code.getSpecialization(), code.getDescription());
        });

        DoctorListResponse doctorsResponse = new DoctorListResponse(doctorsPage.getContent(), doctorsPage.getNumber(),
                doctorsPage.getSize(), doctorsPage.getTotalElements(), doctorsPage.getTotalPages());
        return ResponseEntity.ok(doctorsResponse);
    }
}
