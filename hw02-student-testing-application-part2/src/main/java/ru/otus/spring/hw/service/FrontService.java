package ru.otus.spring.hw.service;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

public interface FrontService {

    void printAllQuestion();

    Student getStudent();

    Answer getAnswer(Question question);

    void printResult();
}
