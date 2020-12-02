package ru.otus.spring.hw.service;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

public interface FrontService {

    void printAllQuestion();

    void getStudentName();

    Answer getAnswer(Question question);

    void printResult();
}
