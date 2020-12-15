package ru.otus.spring.hw.event.events;

import ru.otus.spring.hw.domain.Student;

public class UserLoggingEvent extends CustomEvent {

    private static final long serialVersionUID = 1L;

    public UserLoggingEvent(Object source, Student payload) {
        super(source, payload);
    }

}
