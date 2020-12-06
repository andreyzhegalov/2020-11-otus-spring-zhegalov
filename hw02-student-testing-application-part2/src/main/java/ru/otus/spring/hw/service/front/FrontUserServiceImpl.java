package ru.otus.spring.hw.service.front;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.domain.Student;

public class FrontUserServiceImpl implements FrontUserService {
    private final static String GET_NAME_MESSAGE = "Hello! Please, insert your name";
    private final static String GET_SECOND_NAME_MESSAGE = "Insert your second name";
    private final IOController ioController;

    public FrontUserServiceImpl(IOController ioController) {
        this.ioController = ioController;
    }

    @Override
    public Student getStudent() {
        ioController.print(GET_NAME_MESSAGE);
        final var name = ioController.read();
        ioController.print(GET_SECOND_NAME_MESSAGE);
        final var secondName = ioController.read();
        return new Student(name, secondName);
    }

}
