package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserDetailsRepositoryTest {

    @Autowired
    private UserRepository UserRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void test_findByUsersId(){
        Long id = 1L;
        String email = "test@gmail.com";
        UserDetails expectedUserDetails = addUser(id, email);
        Optional<UserDetails> actualUserDetails = userDetailsRepository.findByUsersId(id);

        assertTrue(actualUserDetails.isPresent());
        assertEquals(expectedUserDetails, actualUserDetails.get());
    }

    // create add user with user details involved;
    private UserDetails addUser(Long id, String email) {
        String password = "test";
        Users user = Users.builder()
                .id(id)
                .email(email)
                .password(password)
                .isConfirmed(true)
                .userType(2)
                .deleted(false)
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .build();

        UserDetails userDetails = UserDetails.builder()
                .id(id)
                .users(user)
                .firstName("test")
                .lastName("test")
                .gender(1)
                .deleted(false)
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .build();

        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);
        testEntityManager.flush();
        return savedUserDetails;
    }
}
