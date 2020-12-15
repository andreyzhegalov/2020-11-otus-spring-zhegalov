package ru.otus.spring.hw.event.events;

import ru.otus.spring.hw.domain.Student;

public class StartQuizEvent extends AbstractCustomEvent {
    private static final long serialVersionUID = 8631480663092425411L;

    public StartQuizEvent(Object source, Student student) {
        super(source, student);
    }

}
