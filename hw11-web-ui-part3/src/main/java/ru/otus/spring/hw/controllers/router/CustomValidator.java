package ru.otus.spring.hw.controllers.router;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomValidator<T> {
    private final Validator validator;

    public void validate(T entity) {
        final var errors = new BeanPropertyBindingResult(entity, "");
        validator.validate(entity, errors);

        String errorMessages = errors.getFieldErrors().stream().map(x -> x.getDefaultMessage())
                .collect(Collectors.joining(","));

        if (Objects.nonNull(errors) && !errors.getAllErrors().isEmpty()) {
            throw new CustomRouterException(HttpStatus.BAD_REQUEST, errorMessages);
        }
    }

}

