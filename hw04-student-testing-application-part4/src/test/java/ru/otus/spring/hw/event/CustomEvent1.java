package ru.otus.spring.hw.event;

import ru.otus.spring.hw.event.events.AbstractCustomEvent;

class CustomEvent1 extends AbstractCustomEvent {

    private static final long serialVersionUID = 4770761090076810329L;

    public CustomEvent1(Object source, Object payload) {
        super(source, payload);
    }
}
