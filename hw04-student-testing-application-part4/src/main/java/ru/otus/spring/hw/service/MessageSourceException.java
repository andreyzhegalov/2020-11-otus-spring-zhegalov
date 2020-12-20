package ru.otus.spring.hw.service;

public class MessageSourceException extends RuntimeException {

    private static final long serialVersionUID = -2558158636513304136L;

    public MessageSourceException() {

    }

    public MessageSourceException(String msg) {
        super(msg);
    }

    public MessageSourceException(Throwable cause) {
        super(cause);
    }
}
