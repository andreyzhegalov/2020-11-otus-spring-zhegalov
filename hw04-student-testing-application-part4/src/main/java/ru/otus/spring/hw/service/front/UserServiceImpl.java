package ru.otus.spring.hw.service.front;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.IOLocalizedService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final static String GET_USER_NAME_KEY = "get.user.name";
    private final static String GET_USER_SECOND_NAME_KEY= "get.user.second.name";

    private final IOLocalizedService ioLocalizedService;

    @Override
    public Student getStudent() {
        ioLocalizedService.printLocalizedMessage(GET_USER_NAME_KEY);
        final var name = ioLocalizedService.read();
        ioLocalizedService.printLocalizedMessage(GET_USER_SECOND_NAME_KEY);
        final var secondName = ioLocalizedService.read();
        return new Student(name, secondName);
    }
}
