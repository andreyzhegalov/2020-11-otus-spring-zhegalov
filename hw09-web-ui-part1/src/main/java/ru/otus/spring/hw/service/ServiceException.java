package ru.otus.spring.hw.service;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -3467770783894235603L;

    public ServiceException(String msg) {
        super(msg);
    }
}
