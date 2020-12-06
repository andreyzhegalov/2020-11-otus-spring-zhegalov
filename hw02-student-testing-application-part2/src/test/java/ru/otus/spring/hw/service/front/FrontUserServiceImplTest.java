package ru.otus.spring.hw.service.front;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.service.IOService;

@ExtendWith(MockitoExtension.class)
public class FrontUserServiceImplTest {

    @Mock
    private IOService ioService;

    @Test
    void getStudentName() {
        new FrontUserServiceImpl(ioService).getStudent();
        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(atLeastOnce()).read();
    }
}
