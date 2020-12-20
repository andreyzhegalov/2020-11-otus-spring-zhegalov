package ru.otus.spring.hw.event.events;

import lombok.Getter;
import ru.otus.spring.hw.domain.Student;

public class UserLoggingEvent extends AbstractCustomEvent {
    private static final long serialVersionUID = 544764458935775198L;
    @Getter
    private final Student student;

    public UserLoggingEvent(Object source, Student student) {
        super(source);
        this.student = student;
    }
}
