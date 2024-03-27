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
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        UserDetails expectedUserDetails = addUser();
        Optional<UserDetails> actualUserDetails = userDetailsRepository.findByUsersId(expectedUserDetails.getUsers().getId());

        assertTrue(actualUserDetails.isPresent());
        assertEquals(expectedUserDetails, actualUserDetails.get());
    }

    @Test
    public void test_findByUsersEmail() {
        UserDetails expectedUserDetails = addUser("user.test@gmail.com");
        UserDetails actualUserDetails = userDetailsRepository.findByUsersEmail(expectedUserDetails.getUsers().getEmail());

        assertNotNull(actualUserDetails);
        assertEquals(expectedUserDetails, actualUserDetails);
    }

    @Test
    public void test_findAllByDoctorCodeIdIsNotNull() {
        Pageable pageable = Pageable.unpaged();

        UserDetails expectedUserDetails = addUserByDoctorCodeId("D13123");
        Page<UserDetails> pagedUserDetails = userDetailsRepository.findAllByDoctorCodeIdIsNotNull(pageable);
        assertTrue(pagedUserDetails.getSize() > 0);

        UserDetails actualUserDetails = pagedUserDetails.getContent().get(0);

        assertEquals(expectedUserDetails, actualUserDetails);
    }

    @Test
    public void test_findByName() {
        Pageable pageable = Pageable.unpaged();
        String name = "test";

        UserDetails expectedUserDetails = addUserByDoctorCodeId("D00001");

        Page<UserDetails> pagedUserDetails = userDetailsRepository.findByName(name, expectedUserDetails.getDoctorCodeId(), pageable);
        UserDetails actualUserDetails = pagedUserDetails.getContent().get(0);

        assertTrue(pagedUserDetails.getSize() > 0);
        assertEquals(expectedUserDetails, actualUserDetails);
    }

    @Test
    public void test_findByName_expectToFail() {
        Pageable pageable = Pageable.unpaged();
        String name = "test2";
        String doctorCode = "D00001";

        addUserByDoctorCodeId(doctorCode);
        Page<UserDetails> pagedUserDetails = userDetailsRepository.findByName(name, doctorCode, pageable);

        assertFalse(pagedUserDetails.getSize() > 0);
    }

    @Test
    public void test_findByFullName() {
        Pageable pageable = Pageable.unpaged();
        String firstname = "test";
        String lastname = "test";
        String doctorCode = "D00001";

        UserDetails expectedUserDetails = addUserByDoctorCodeId(doctorCode);
        Page<UserDetails> pagedUserDetails = userDetailsRepository.findByFullName(firstname, lastname, doctorCode, pageable);
        assertTrue(pagedUserDetails.getSize() > 0);

        UserDetails actualUserDetails = pagedUserDetails.getContent().get(0);

        assertEquals(expectedUserDetails, actualUserDetails);
    }

    @Test
    public void test_findByFullName_expectToFail(){
        Pageable pageable = Pageable.unpaged();
        String firstname = "test2";
        String lastname = "test3";
        String doctorCode = "D00001";

        addUserByDoctorCodeId(doctorCode);
        Page<UserDetails> pagedUserDetails = userDetailsRepository.findByFullName(firstname, lastname, doctorCode, pageable);

        assertFalse(pagedUserDetails.getSize() > 0);
    }

    private UserDetails addUser(String email, String doctorCodeId) {
        String password = "test";
        Users user = Users.builder()
                .email(email)
                .password(password)
                .isConfirmed(true)
                .userType(2)
                .deleted(false)
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .build();

        UserDetails userDetails = UserDetails.builder()
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

    private UserDetails addUser() {
        return addUser("test@gmail.com", null);
    }
    private UserDetails addUser(String email) {
        return addUser(email, null);
    }

    private UserDetails addUserByDoctorCodeId(String doctorCodeId) {
        return addUser("test@gmail.com", doctorCodeId);
    }
}
