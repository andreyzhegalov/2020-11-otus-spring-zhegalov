package ru.otus.spring.hw.event;

public interface EventPublisher<T> {

    void publish(T event);

}

