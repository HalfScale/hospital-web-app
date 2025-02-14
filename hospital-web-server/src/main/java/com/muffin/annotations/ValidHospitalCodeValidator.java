package com.muffin.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.muffin.model.DoctorCode;
import com.muffin.repository.DoctorCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ValidHospitalCodeValidator implements ConstraintValidator<ValidHospitalCode, String> {

    @Autowired
    private final DoctorCodeRepository doctorCodeRepository;

    @Override
    public boolean isValid(String targetCode, ConstraintValidatorContext context) {
        if (targetCode == null || targetCode.trim().isEmpty()) {
            return true;
        }

        Optional<DoctorCode> doctorCode = doctorCodeRepository.findTopByCodeOrderByCreatedDesc(targetCode);


        return doctorCode.isPresent() && !doctorCode.get().isDeleted();
    }
}

