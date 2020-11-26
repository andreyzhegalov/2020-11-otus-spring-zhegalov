package ru.otus.spring.hw.dao;

import java.util.Optional;

import ru.otus.spring.hw.domain.Question;

public class CsvQuestionDao implements QuestionDao {

    @Override
    public Optional<Question> getQuestion(int number) {
        return Optional.empty();
    }

}
