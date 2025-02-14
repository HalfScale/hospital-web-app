package com.muffin.validators;

import com.muffin.annotations.PasswordMatchValidator;
import com.muffin.model.dto.UserRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PasswordMatchValidatorTest {

    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

    private PasswordMatchValidator passwordMatchValidator;

    @BeforeEach
    public void setUp() {
        passwordMatchValidator = new PasswordMatchValidator();

    }

    @Test
    public void passwordMatchValidator_ShouldPass() {

        UserRegistration userRegistration = UserRegistration.builder()
                .password("matchingpass")
                .confirmPassword("matchingpass")
                .build();

        assertTrue(passwordMatchValidator.isValid(userRegistration, context));
    }

    @Test
    public void passwordMatchValidator_ShouldFail() {
        UserRegistration userRegistration = UserRegistration.builder()
                .password("notmachingPass")
                .confirmPassword("notmachingPass2")
                .build();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilderCustomizableContext);
        when(nodeBuilderCustomizableContext.addConstraintViolation()).thenReturn(context);

        assertFalse(passwordMatchValidator.isValid(userRegistration, context));
    }
}
