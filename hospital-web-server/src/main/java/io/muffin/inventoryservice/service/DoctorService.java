package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.model.DoctorCode;
import io.muffin.inventoryservice.model.dto.DoctorCardResponse;
import io.muffin.inventoryservice.repository.DoctorCodeRepository;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorCodeRepository doctorCodeRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final ModelMapper modelMapper;

    public Page<DoctorCardResponse> findAllDoctor(String name, String doctorCode, Pageable pageable) {

        name = StringUtils.hasText(name) ? name: "";
        doctorCode = StringUtils.hasText(doctorCode) ? doctorCode : "";

        if(StringUtils.hasText(name) || StringUtils.hasText(doctorCode)) {
            log.info("USER name=> [{}], doctorCode => [{}]", name, doctorCode);
            String[] splitedName = name.split(" ");
            String firstName = splitedName[0];
            String lastName = splitedName[1];
        }

        return userDetailsRepository.findAllByDoctorCodeIdIsNotNull(pageable).map(user -> {
            log.info("USER ID=> [{}], CODE => [{}]", user.getUsers().getId(), user.getDoctorCodeId());
            DoctorCode code = doctorCodeRepository.findByDoctorCode(user.getDoctorCodeId()).orElse(null);
            return new DoctorCardResponse(String.format("%s %s", user.getFirstName(), user.getLastName()),
                    user.getProfileImage(), code.getSpecialization(), code.getDescription());
        });
    }
}
