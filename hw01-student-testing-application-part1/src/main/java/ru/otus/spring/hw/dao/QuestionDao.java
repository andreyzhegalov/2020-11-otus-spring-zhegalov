package ru.otus.spring.hw.dao;

import java.util.Optional;

import ru.otus.spring.hw.domain.Question;

public interface QuestionDao {

    Optional<Question> getFirstQuestion();

    Optional<Question> getQuestion(int number);
}

