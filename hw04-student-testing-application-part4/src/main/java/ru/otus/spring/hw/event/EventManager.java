package ru.otus.spring.hw.event;

import java.util.function.Consumer;

public interface EventManager<T> {

    void connect(Class<? extends T> eventType, Consumer<? super T> consumer);

}

