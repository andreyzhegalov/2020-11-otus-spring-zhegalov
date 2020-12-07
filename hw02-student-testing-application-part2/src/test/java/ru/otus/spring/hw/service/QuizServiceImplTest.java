package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.front.FrontService;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    @Mock
    private FrontService frontService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private ReportPrintService reportPrintService;

    @Test
    void shouldPrintAllQuestion() {
        final var answer = new Answer("1");
        final var question = new Question(1, "question", answer);

        given(questionDao.getAllQuestion()).willReturn(Arrays.asList(question, question));

        new QuizServiceImpl(questionDao, frontService, reportPrintService).printAllQuestion();

        then(questionDao).should().getAllQuestion();
        then(questionDao).shouldHaveNoMoreInteractions();
        then(frontService).should(times(2)).getAnswer(any());
        then(frontService).shouldHaveNoMoreInteractions();
    }

    @Test
    void startTestingForOneTestSuite() {
        final var answer = new Answer("1");
        final var question = new Question(1, "question", answer);

        final var student = new Student("ivan", "ivanov");

        given(frontService.getStudent()).willReturn(student);
        given(questionDao.getAllQuestion()).willReturn(Collections.singletonList(question));
        given(frontService.getAnswer(question)).willReturn(answer);

        new QuizServiceImpl(questionDao, frontService, reportPrintService).startTesting();

        then(frontService).should().getStudent();
        then(questionDao).should().getAllQuestion();
        then(questionDao).shouldHaveNoMoreInteractions();
        then(frontService).should().getAnswer(any());
        then(reportPrintService).should().addAnswer(student, question, answer);
        then(reportPrintService).should().makeReport(student);
        then(reportPrintService).shouldHaveNoMoreInteractions();
        then(frontService).should().printResult(any());
        then(frontService).shouldHaveNoMoreInteractions();
    }
}
