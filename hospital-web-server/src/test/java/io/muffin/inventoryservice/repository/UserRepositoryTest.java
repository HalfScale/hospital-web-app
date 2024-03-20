package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void test_findByEmail(){
        // testing findByEmail
        String userEmail = "user.test.1@gmail.com";
        addUser(userEmail);
        Optional<Users> persistedUser = UserRepository.findByEmail(userEmail);

        assertTrue(persistedUser.isPresent());
        assertEquals(userEmail, persistedUser.get().getEmail());
    }

    private void addUser(String email) {
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

        UserRepository.save(user);
        testEntityManager.flush();
    }
}
