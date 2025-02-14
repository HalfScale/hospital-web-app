package com.muffin.validators;

import com.muffin.annotations.UniqueEmailValidator;
import com.muffin.model.Users;
import com.muffin.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UniqueEmailValidatorTest {

    @Mock
    private UserRepository userRepository;

    private UniqueEmailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueEmailValidator(userRepository);
    }

    @Test
    void shouldPassForUniqueEmail() {
        when(userRepository.findByEmail("newemail@example.com")).thenReturn(Optional.empty());

        assertTrue(validator.isValid("newemail@example.com", null));
    }

    @Test
    void shouldPassForDeletedEmail() {
        when(userRepository.findByEmail("deletedEmail@example.com")).thenReturn(Optional.empty());

        assertTrue(validator.isValid("deletedEmail@example.com", null));
    }

    @Test
    void shouldFailForExistingEmail() {
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new Users()));

        assertFalse(validator.isValid("existing@example.com", null));
    }
}

