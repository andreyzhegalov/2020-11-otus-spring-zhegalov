package ru.otus.spring.hw.controllers;

public class ControllerException extends RuntimeException {

    private static final long serialVersionUID = -6590965697334666046L;

    public ControllerException(String msg) {
        super(msg);
    }
}
