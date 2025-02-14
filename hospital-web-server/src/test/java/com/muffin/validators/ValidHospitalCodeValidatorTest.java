package com.muffin.validators;

import com.muffin.annotations.ValidHospitalCodeValidator;
import com.muffin.model.DoctorCode;
import com.muffin.repository.DoctorCodeRepository;
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
public class ValidHospitalCodeValidatorTest {

    @Mock
    private DoctorCodeRepository doctorCodeRepository;

    private ValidHospitalCodeValidator validHospitalCodeValidator;

    @BeforeEach
    public void setUp() {
        validHospitalCodeValidator = new ValidHospitalCodeValidator(doctorCodeRepository);
    }

    @Test
    public void isDoctorCodeValid_ShouldPass() {
        when(doctorCodeRepository.findTopByCodeOrderByCreatedDesc("DOCTORCODE")).thenReturn(Optional.of(new DoctorCode()));

        assertTrue(validHospitalCodeValidator.isValid("DOCTORCODE", null));
    }

    @Test
    public void isDoctorCodeValid_ShouldFail() {
        when(doctorCodeRepository.findTopByCodeOrderByCreatedDesc("INVALIDCODE")).thenReturn(Optional.empty());

        assertFalse(validHospitalCodeValidator.isValid("INVALIDCODE", null));
    }
}
