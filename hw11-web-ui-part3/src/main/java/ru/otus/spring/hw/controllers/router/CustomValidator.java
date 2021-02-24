package ru.otus.spring.hw.controllers.router;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomValidator<T> {
    private final Validator validator;

    public void validate(T entity) {
        final var errors = new BeanPropertyBindingResult(entity, "");
        validator.validate(entity, errors);

        String errorMessages = errors.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));

        if (!errors.getAllErrors().isEmpty()) {
            log.error(errorMessages);
            throw new CustomRouterException(errorMessages);
        }
    }
}
