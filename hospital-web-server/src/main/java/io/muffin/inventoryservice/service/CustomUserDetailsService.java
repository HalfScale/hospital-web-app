package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.Users;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("AUTHENTICATE_USER => [{}]", email);
        Users users = userRepository.findByEmail(email).orElse(null);
        io.muffin.inventoryservice.model.UserDetails userDetails = userDetailsRepository.findByUsersId(users.getId()).orElse(null);

        if(users == null || userDetails == null) {
            log.info("EMAIL_NOT_FOUND => [{}]", email);
            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", email));
        }

        String name = String.format("%s %s", userDetails.getFirstName(), userDetails.getLastName());
        return new JwtUserDetails(users.getId(), users.getEmail(), users.getPassword(), name, userDetails.getProfileImage(), users.getAuthorities().getName());
    }
}
