package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void test_SaveUser() {
        String email = "test@email.com";
        String password = "test";
        Users user = Users.builder()
                .id(0L)
                .email(email)
                .password(password)
                .isConfirmed(true)
                .userType(2)
                .deleted(false)
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .build();

        Users savedUser = UserRepository.save(user);
        testEntityManager.flush();

        Users fetchedUser = testEntityManager.find(Users.class, savedUser.getId());

        assertNotNull(fetchedUser);
        assertNotNull(fetchedUser.getId());
        assertEquals(email, fetchedUser.getEmail());
        assertTrue(fetchedUser.isConfirmed());
        assertTrue(passwordEncoder.matches(password, passwordEncoder.encode(fetchedUser.getPassword())));

    }
}
