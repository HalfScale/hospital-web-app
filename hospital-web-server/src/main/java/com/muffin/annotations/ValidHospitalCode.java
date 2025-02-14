package com.muffin.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidHospitalCodeValidator.class)
@Target(ElementType.FIELD) // Apply on a field
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHospitalCode {
    String message() default "Invalid hospital code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

