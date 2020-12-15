package ru.otus.spring.hw.event;

import ru.otus.spring.hw.event.events.AbstractCustomEvent;

class CustomEvent2 extends AbstractCustomEvent {
    private static final long serialVersionUID = 5485313498638066867L;

    public CustomEvent2(Object source, Object payload) {
        super(source, payload);
    }
}
