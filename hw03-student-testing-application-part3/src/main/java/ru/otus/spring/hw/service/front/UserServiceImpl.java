package ru.otus.spring.hw.service.front;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ru.otus.spring.hw.config.AppProps;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.IOService;

@Service
public class UserServiceImpl implements UserService {
    private final IOService ioService;
    private final MessageSource messageSource;
    private final AppProps props;

    public UserServiceImpl(IOService ioService, MessageSource messageSource, AppProps props) {
        this.ioService = ioService;
        this.messageSource = messageSource;
        this.props = props;
    }

    @Override
    public Student getStudent() {
        var message = messageSource.getMessage("get.user.name", new String[0], props.getLocale());
        ioService.print(message);
        final var name = ioService.read();

        message = messageSource.getMessage("get.user.second.name", new String[0], props.getLocale());
        ioService.print(message);
        final var secondName = ioService.read();
        return new Student(name, secondName);
    }
}
