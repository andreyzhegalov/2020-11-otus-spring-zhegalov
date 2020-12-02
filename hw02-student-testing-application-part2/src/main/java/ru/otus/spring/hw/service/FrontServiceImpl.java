package ru.otus.spring.hw.service;

import java.util.ArrayList;
import java.util.List;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

public class FrontServiceImpl implements FrontService {
    private final IOController ioController;
    private final QuizService quizService;

    public FrontServiceImpl(final QuizService quizService, final IOController ioService) {
        this.quizService = quizService;
        this.ioController = ioService;
    }

    @Override
    public void printAllQuestion() {
        final List<Question> allQuestions = getAllQuestion();
        allQuestions.forEach(question -> ioController.print(question.getText()));
    }

    private List<Question> getAllQuestion() {
        final var resultList = new ArrayList<Question>();
        var mayBeQuestion = quizService.getNextQuestion(null);
        while (mayBeQuestion.isPresent()) {
            resultList.add(mayBeQuestion.get());
            mayBeQuestion = quizService.getNextQuestion(mayBeQuestion.get());
            if (mayBeQuestion.isEmpty()) {
                break;
            }
        }
        return resultList;
    }

    @Override
    public Answer getAnswer(final Question question) {
        ioController.print(question.getText());
        final var answerText = ioController.read();
        return new Answer(answerText);
    }

    @Override
    public void printResult() {
        // TODO Auto-generated method stub
    }

    @Override
    public Student getStudent() {
        final String PROMT_MESSAGE = "Hello! Please, insert your name";
        ioController.print(PROMT_MESSAGE);
        final var answer = ioController.read();
        return new Student(answer);
    }
}
