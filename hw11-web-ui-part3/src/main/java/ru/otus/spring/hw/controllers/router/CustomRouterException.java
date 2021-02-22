package ru.otus.spring.hw.controllers.router;

import lombok.Getter;

@Getter
public class CustomRouterException extends RuntimeException {
    private static final long serialVersionUID = -3175505751677491423L;

    public CustomRouterException(Throwable e) {
        super(e);
    }

    public CustomRouterException(String message) {
        super(message);
    }
}
