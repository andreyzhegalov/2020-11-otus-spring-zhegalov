package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.front.FrontService;

@SpringBootTest
class QuizServiceImplTest {

    @Import(QuizServiceImpl.class)
    @Configuration
    public static class QuizServiceImplInner {
    }

    @MockBean
    private FrontService frontService;

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private QuizService quizService;

    @Test
    void shouldPrintAllQuestion() {
        final var answer = new Answer("1");
        final var question = new Question(1, "question", answer);

        given(questionDao.getAllQuestion()).willReturn(Arrays.asList(question, question));

        quizService.printAllQuestion();

        then(questionDao).should().getAllQuestion();
        then(questionDao).shouldHaveNoMoreInteractions();
        then(frontService).should(times(2)).getAnswer(any());
        then(frontService).shouldHaveNoMoreInteractions();
    }

    @Test
    void quizServiceShouldReturnReport() {
        final var answer = new Answer("1");
        final var question = new Question(1, "question", answer);
        final var student = new Student("ivan", "ivanov");

        given(frontService.getStudent()).willReturn(student);
        given(questionDao.getAllQuestion()).willReturn(Collections.singletonList(question));
        given(frontService.getAnswer(question)).willReturn(answer);

        final var report = quizService.startTesting(student);

        assertThat(report.getStudent()).isEqualTo(student);
        assertThat(report.getResult()).isNotEmpty();

        then(questionDao).should().getAllQuestion();
        then(questionDao).shouldHaveNoMoreInteractions();
        then(frontService).should().getAnswer(any());
        then(frontService).shouldHaveNoMoreInteractions();
    }
}
