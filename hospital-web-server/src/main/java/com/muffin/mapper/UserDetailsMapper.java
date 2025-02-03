package com.muffin.mapper;

import com.muffin.model.UserDetails;
import com.muffin.model.dto.UserRegistration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class UserDetailsMapper {

    public UserDetails mapToUserDetails(UserRegistration userRegistration) {
        return UserDetails.builder()
                .firstName(userRegistration.getFirstName())
                .lastName(userRegistration.getLastName())
                .mobileNo(userRegistration.getMobileNo())
                .doctorCodeId(userRegistration.getHospitalCode())
                .gender(userRegistration.getGender())
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .deleted(false)
                .build();
    }
}
