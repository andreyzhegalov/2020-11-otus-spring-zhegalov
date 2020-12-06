package ru.otus.spring.hw.service.front;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
        given(ioService.read()).willReturn("ivan");
        given(ioService.read()).willReturn("ivanov");

        final var student = new FrontUserServiceImpl(ioService).getStudent();
        assertThat(student).isNotNull();

        then(ioService).should(times(2)).print(anyString());
        then(ioService).should(times(2)).read();
    }
}
