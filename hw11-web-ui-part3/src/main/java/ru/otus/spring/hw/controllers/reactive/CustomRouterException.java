package ru.otus.spring.hw.controllers.reactive;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.Getter;

@Getter
public class CustomRouterException extends ResponseStatusException {
    private final String message;

    public CustomRouterException(HttpStatus status, String message) {
        super(status, message);
        this.message = message;
    }
}
