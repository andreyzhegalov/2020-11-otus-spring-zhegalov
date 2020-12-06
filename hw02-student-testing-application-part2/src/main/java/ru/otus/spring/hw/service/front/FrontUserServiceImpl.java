package ru.otus.spring.hw.service.front;

import ru.otus.spring.hw.service.IOService;
import ru.otus.spring.hw.domain.Student;

public class FrontUserServiceImpl implements FrontUserService {
    private final static String GET_NAME_MESSAGE = "Hello! Please, insert your name";
    private final static String GET_SECOND_NAME_MESSAGE = "Insert your second name";
    private final IOService ioService;

    public FrontUserServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Student getStudent() {
        ioService.print(GET_NAME_MESSAGE);
        final var name = ioService.read();
        ioService.print(GET_SECOND_NAME_MESSAGE);
        final var secondName = ioService.read();
        return new Student(name, secondName);
    }

}
