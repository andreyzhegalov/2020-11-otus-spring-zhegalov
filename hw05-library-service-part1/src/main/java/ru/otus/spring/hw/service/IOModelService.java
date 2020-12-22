package ru.otus.spring.hw.service;

import java.util.List;

public interface IOModelService<T> {

    void print(List<T> model);

    T get();
}
