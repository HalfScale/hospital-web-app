package com.muffin.service;

import com.muffin.model.UserDetails;
import com.muffin.jwt.JwtUserDetails;
import com.muffin.model.Users;
import com.muffin.repository.UserDetailsRepository;
import com.muffin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("AUTHENTICATE_USER => [{}]", email);
        Users users = userRepository.findByEmail(email).orElse(null);
        UserDetails userDetails = userDetailsRepository.findByUsersId(users.getId()).orElse(null);

        if(users == null || userDetails == null) {
            log.info("EMAIL_NOT_FOUND => [{}]", email);
            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", email));
        }

        String name = String.format("%s %s", userDetails.getFirstName(), userDetails.getLastName());
        return new JwtUserDetails(users.getId(), users.getEmail(), users.getPassword(), name, users.getAuthorities().getName());
    }
}
