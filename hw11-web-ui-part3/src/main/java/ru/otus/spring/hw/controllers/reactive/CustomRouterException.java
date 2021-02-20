package ru.otus.spring.hw.controllers.reactive;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.Getter;

@Getter
public class CustomRouterException extends ResponseStatusException {
    private static final long serialVersionUID = -3175505751677491423L;
    private final String message;

    public CustomRouterException(HttpStatus status, String message) {
        super(status, message);
        this.message = message;
    }
}
