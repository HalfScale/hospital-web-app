package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.Roles;
import io.muffin.inventoryservice.model.User;
import io.muffin.inventoryservice.repository.RolesRepository;
import io.muffin.inventoryservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);

        if(user == null) {
            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
        }

        return new JwtUserDetails(user.getId(),user.getUsername(), user.getPassword(), user.getRole().getName());
    }
}
