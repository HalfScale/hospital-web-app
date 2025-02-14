package com.muffin.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.muffin.model.Users;
import com.muffin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String targetEmail, ConstraintValidatorContext context) {
        if (targetEmail == null || targetEmail.trim().isEmpty()) {
            return false;
        }

        Optional<Users> queriedEmail = userRepository.findTopByEmailOrderByCreatedDesc(targetEmail);

        return queriedEmail.isEmpty() || queriedEmail.get().isDeleted();
    }
}

