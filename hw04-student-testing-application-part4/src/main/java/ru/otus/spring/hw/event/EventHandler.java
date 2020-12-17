package ru.otus.spring.hw.event;

public interface EventHandler<T> {

    void handle(T event);

}

