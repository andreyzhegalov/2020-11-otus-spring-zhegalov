package ru.otus.spring.hw.service;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

public class FrontServiceImpl implements FrontService {
    private final IOController ioController;

    public FrontServiceImpl(IOController ioService) {
        this.ioController = ioService;
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
