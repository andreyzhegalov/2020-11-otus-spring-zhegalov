package ru.otus.spring.hw.service.front;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.IOLocalizedService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final IOLocalizedService ioLocalizedService;

    @Override
    public Student getStudent() {
        ioLocalizedService.printLocalizedMessage("get.user.name");
        final var name = ioLocalizedService.read();
        ioLocalizedService.printLocalizedMessage("get.user.second.name");
        final var secondName = ioLocalizedService.read();
        return new Student(name, secondName);
    }
}
