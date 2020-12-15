package ru.otus.spring.hw.event.events;

import ru.otus.spring.hw.domain.Student;

public class UserLoggingEvent extends AbstractCustomEvent {
    private static final long serialVersionUID = 544764458935775198L;

    public UserLoggingEvent(Object source, Student payload) {
        super(source, payload);
    }

}
