package ru.otus.spring.hw.service.front;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.IOLocalizedService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final IOLocalizedService ioLocalizesService;

    @Override
    public Student getStudent() {
        ioLocalizesService.print("get.user.name");
        final var name = ioLocalizesService.read();
        ioLocalizesService.print("get.user.second.name");
        final var secondName = ioLocalizesService.read();
        return new Student(name, secondName);
    }
}
