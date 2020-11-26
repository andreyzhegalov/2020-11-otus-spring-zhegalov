package ru.otus.spring.hw.service;

import java.util.Optional;

import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Result;

public class QuizServiceImpl implements QuizService {
    private final QuestionDao questionDao;

    public QuizServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public Optional<Question> getNextQuestion(Question lastQuestion) {
        if (lastQuestion == null) {
            return questionDao.getQuestion(0);
        }
        final var nextNumber = lastQuestion.getNumber() + 1;
        return questionDao.getQuestion(nextNumber);
    }

    @Override
    public Result checkAnswer(Question question, Answer answer) {
        throw new UnsupportedOperationException();
    }
}
