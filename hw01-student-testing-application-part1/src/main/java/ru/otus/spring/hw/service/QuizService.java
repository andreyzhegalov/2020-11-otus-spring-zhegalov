package ru.otus.spring.hw.service;

import java.util.Optional;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Result;

public interface QuizService {

    Optional<Question> getNextQuestion(Question lastQuestion);

    Result checkAnswer(Question question, Answer answer);
}

