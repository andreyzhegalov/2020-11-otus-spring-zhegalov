package ru.otus.spring.hw.service;

public interface IOLocalizedService {

    void printLocalizedMessage(String key, Object... args);

    void print(String text);

    String read();

}
