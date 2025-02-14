package com.muffin.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.muffin.model.dto.UserRegistration;
import org.springframework.util.StringUtils;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserRegistration> {

    @Override
    public boolean isValid(UserRegistration userRegistration, ConstraintValidatorContext context) {
        if (userRegistration == null) return true;

        if (!StringUtils.hasText(userRegistration.getPassword()) || !StringUtils.hasText(userRegistration.getConfirmPassword())) {
            return true;
        }

        boolean isValid = userRegistration.getPassword().equals(userRegistration.getConfirmPassword());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords must match")
                    .addPropertyNode("confirmPassword") // Target confirmPassword field
                    .addConstraintViolation();
        }

        return isValid;
    }
}



