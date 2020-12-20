package ru.otus.spring.hw.event.events;

import org.springframework.context.ApplicationEvent;

public abstract class AbstractCustomEvent extends ApplicationEvent {
    private static final long serialVersionUID = -8787487454825799696L;

    public AbstractCustomEvent(Object source) {
        super(source);
    }
}
