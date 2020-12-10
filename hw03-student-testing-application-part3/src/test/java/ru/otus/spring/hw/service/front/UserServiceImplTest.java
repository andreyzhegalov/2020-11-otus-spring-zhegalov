package ru.otus.spring.hw.service.front;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.service.IOLocalizedService;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private IOLocalizedService ioLocalizeService;

    @Test
    void getStudentName() {
        new UserServiceImpl(ioLocalizeService).getStudent();
        then(ioLocalizeService).should(atLeastOnce()).print(anyString(), any());
        then(ioLocalizeService).should(atLeastOnce()).read();
    }
}
