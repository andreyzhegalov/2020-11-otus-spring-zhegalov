package ru.otus.spring.hw.service;

import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

public interface QuizService {

    Report startTesting(Student student);

    void printAllQuestion();
}
