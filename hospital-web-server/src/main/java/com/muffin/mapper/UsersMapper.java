package com.muffin.mapper;

import com.muffin.model.Authorities;
import com.muffin.model.Users;
import com.muffin.model.dto.UserRegistration;
import com.muffin.repository.AuthoritiesRepository;
import com.muffin.utility.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class UsersMapper {

    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder encoder;

    public Users mapToUsers(UserRegistration userRegistration) {
        int userType = getUserType(userRegistration);
        return Users.builder()
                .email(userRegistration.getEmail())
                .password(encoder.encode(userRegistration.getPassword()))
                .userType(userType)
                .authorities(getUserAuthority(userType))
                .isConfirmed(true)
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .enabled(true)
                .deleted(false)
                .build();
    }

    private int getUserType(UserRegistration userRegistration) {
        return StringUtils.hasText(userRegistration.getHospitalCode()) ? Constants.USER_DOCTOR : Constants.USER_PATIENT;
    }

    private Authorities getUserAuthority(int userType) {
        String authority = userType == Constants.USER_DOCTOR ? Constants.AUTHORITY_DOCTOR : Constants.AUTHORITY_PATIENT;
        return authoritiesRepository.findByName(authority)
                .orElseThrow(() -> new RuntimeException("Invalid authority"));
    }
}
