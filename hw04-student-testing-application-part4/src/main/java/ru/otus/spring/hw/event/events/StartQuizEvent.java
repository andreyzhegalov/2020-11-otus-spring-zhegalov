package ru.otus.spring.hw.event.events;

import ru.otus.spring.hw.domain.Student;

public class StartQuizEvent extends CustomEvent {

    private static final long serialVersionUID = 1L;

    public StartQuizEvent(Object source, Student student) {
        super(source, student);
    }

}
