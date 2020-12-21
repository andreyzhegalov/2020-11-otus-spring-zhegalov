package ru.otus.spring.hw.service;

public class ServiceException extends RuntimeException {

    public ServiceException() {
    }

    public ServiceException(String msg) {
        super(msg);
    }
}

