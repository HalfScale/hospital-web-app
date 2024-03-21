package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        UserDetails expectedUserDetails = addUser(id);
        Optional<UserDetails> actualUserDetails = userDetailsRepository.findByUsersId(id);

        assertTrue(actualUserDetails.isPresent());
        assertEquals(expectedUserDetails, actualUserDetails.get());
    }

    @Test
    public void test_findByUsersEmail() {
        String email = "user.test@gmail.com";
        UserDetails expectedUserDetails = addUser(email);
        UserDetails actualUserDetails = userDetailsRepository.findByUsersEmail(email);

        assertNotNull(actualUserDetails);
        assertEquals(expectedUserDetails, actualUserDetails);
    }

    @Test
    public void test_findAllByDoctorCodeIdIsNotNull() {
        String email = "user.test@gmail.com";
        Pageable pageable = Pageable.unpaged();

        addUserByDoctorCodeId("D13123");
        Page<UserDetails> pagedUserDetails = userDetailsRepository.findAllByDoctorCodeIdIsNotNull(pageable);


        assertTrue(pagedUserDetails.getSize() > 0);
//        assertEquals(expectedUserDetails, actualUserDetails);
    }

    private UserDetails addUser(Long id, String email, String doctorCodeId) {
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
                .doctorCodeId(doctorCodeId)
                .deleted(false)
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .build();

        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);
        testEntityManager.flush();
        return savedUserDetails;
    }

    private UserDetails addUser(Long id) {
        return addUser(id, "test@gmail.com", null);
    }
    private UserDetails addUser(String email) {
        return addUser(1L, email, null);
    }

    private UserDetails addUserByDoctorCodeId(String doctorCodeId) {
        return addUser(1L, "test@gmail.com", doctorCodeId);
    }

    private void addUser() {

    }
}
