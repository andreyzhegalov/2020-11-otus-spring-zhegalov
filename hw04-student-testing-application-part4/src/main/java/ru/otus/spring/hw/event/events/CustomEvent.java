package ru.otus.spring.hw.event.events;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

public abstract class CustomEvent extends ApplicationEvent {
    @Getter
    private final Object payload;

    public CustomEvent(Object source, Object payload) {
        super(source);
        this.payload = payload;
    }
}

