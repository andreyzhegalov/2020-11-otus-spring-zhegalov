package ru.otus.spring.hw.service.front;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

public interface FrontService {

    Student getStudent();

    Answer getAnswer(Question question);

    void printResult(Report report);
}
