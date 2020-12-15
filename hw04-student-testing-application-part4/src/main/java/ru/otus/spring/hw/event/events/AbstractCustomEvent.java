package ru.otus.spring.hw.event.events;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

public abstract class AbstractCustomEvent extends ApplicationEvent {
    private static final long serialVersionUID = -8787487454825799696L;
    @Getter
    private final Object payload;

    public AbstractCustomEvent(Object source, Object payload) {
        super(source);
        this.payload = payload;
    }
}
