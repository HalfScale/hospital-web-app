package io.muffin.inventoryservice.utility;

import io.muffin.inventoryservice.model.dto.UserRegistration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.*;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GlobalFieldValidator {

    private final Validator validator;

    public <T> void validate(T objectToValidate) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);

        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
