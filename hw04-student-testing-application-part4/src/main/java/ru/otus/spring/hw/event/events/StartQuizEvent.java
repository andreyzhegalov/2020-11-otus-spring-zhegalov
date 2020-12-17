package ru.otus.spring.hw.event.events;

import lombok.Getter;
import ru.otus.spring.hw.domain.Student;

public class StartQuizEvent extends AbstractCustomEvent {
    private static final long serialVersionUID = 8631480663092425411L;
    @Getter
    private final Student student;

    public StartQuizEvent(Object source, Student student) {
        super(source);
        this.student = student;
    }

}
