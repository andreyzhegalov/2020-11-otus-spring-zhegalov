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

import ru.otus.spring.hw.controller.IOController;

@ExtendWith(MockitoExtension.class)
public class FrontUserServiceImplTest {

    @Mock
    private IOController ioController;

    @Test
    void getStudentName() {
        given(ioController.read()).willReturn("ivan");
        given(ioController.read()).willReturn("ivanov");

        final var student = new FrontUserServiceImpl(ioController).getStudent();
        assertThat(student).isNotNull();

        then(ioController).should(times(2)).print(anyString());
        then(ioController).should(times(2)).read();
    }
}
