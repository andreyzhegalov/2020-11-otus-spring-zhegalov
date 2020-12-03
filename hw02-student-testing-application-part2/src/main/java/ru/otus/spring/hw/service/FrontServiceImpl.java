package ru.otus.spring.hw.service;

import java.util.ArrayList;
import java.util.List;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

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
    public Student getStudent() {
        final String GET_NAME_MESSAGE = "Hello! Please, insert your name";
        final String GET_SECOND_NAME_MESSAGE = "Insert your second name";
        ioController.print(GET_NAME_MESSAGE);
        final var name = ioController.read();
        ioController.print(GET_SECOND_NAME_MESSAGE);
        final var secondName = ioController.read();
        return new Student(name, secondName);
    }

    @Override
    public void printResult(Report report) {
        ioController.print(report.print());
    }
}
