package ru.otus.spring.hw.service;

public interface IOLocalizedService {

    void print(String key, Object... args);

    String read();

}
