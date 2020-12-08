package ru.otus.spring.hw.service.front;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import ru.otus.spring.hw.config.AppProps;
import ru.otus.spring.hw.service.IOService;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private MessageSource messageSource;

    private AppProps props;

    @BeforeEach
    void setUp() {
        props = new AppProps();
        props.setLocale(Locale.US);
    }

    @Test
    void getStudentName() {
        given(messageSource.getMessage(anyString(), any(), any())).willReturn("");
        new UserServiceImpl(ioService, messageSource, props).getStudent();
        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(atLeastOnce()).read();
    }
}
