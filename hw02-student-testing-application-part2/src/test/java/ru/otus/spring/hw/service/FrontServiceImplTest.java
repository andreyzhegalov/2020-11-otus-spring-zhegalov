package ru.otus.spring.hw.service;

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
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

@ExtendWith(MockitoExtension.class)
class FrontServiceImplTest {

    @Mock
    private IOController ioController;

    @Test
    void getStudentName() {
        given(ioController.read()).willReturn("ivan");
        given(ioController.read()).willReturn("ivanov");

        final var student = new FrontServiceImpl(ioController).getStudent();
        assertThat(student).isNotNull();

        then(ioController).should(times(2)).print(anyString());
        then(ioController).should(times(2)).read();
    }

    @Test
    void getAnswer() {
        final var question = new Question(1, "text", new Answer(""));
        final var answer = new FrontServiceImpl(ioController).getAnswer(question);
        assertThat(answer).isNotNull();

        then(ioController).should().print(question.getText());
        then(ioController).should().read();
    }

    @Test
    void printResult() {
        final var report = new Report(new Student("ivan", "ivanov"));
        new FrontServiceImpl(ioController).printResult(report);

        then(ioController).should().print(anyString());
    }
}
