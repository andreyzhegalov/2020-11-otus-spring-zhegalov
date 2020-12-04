package ru.otus.spring.hw.service;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

@Service
public class FrontServiceImpl implements FrontService {
    private final static String GET_NAME_MESSAGE = "Hello! Please, insert your name";
    private final static String GET_SECOND_NAME_MESSAGE = "Insert your second name";
    private final IOController ioController;

    public FrontServiceImpl(IOController ioController) {
        this.ioController = ioController;
    }

    @Override
    public Answer getAnswer(final Question question) {
        ioController.print(question.getText());
        final var answerText = ioController.read();
        return new Answer(answerText);
    }

    @Override
    public Student getStudent() {
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
