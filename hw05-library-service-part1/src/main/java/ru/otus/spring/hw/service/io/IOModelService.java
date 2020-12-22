package ru.otus.spring.hw.service.io;

import java.util.List;

public interface IOModelService<T> {

    void print(List<T> model);

    T get();
}
