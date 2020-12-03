package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Student;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    @Mock
    private FrontService frontService;

    @Mock
    private QuestionDao questionDao;

    @Test
    void shouldPrintAllQuestion(){
        final var answer = new Answer("1");
        final var question = new Question(1, "question", answer);

        given(questionDao.getAllQuestion()).willReturn(Arrays.asList(question, question));

        new QuizServiceImpl(questionDao, frontService).printAllQuestion();

        then(questionDao).should().getAllQuestion();
        then(questionDao).shouldHaveNoMoreInteractions();
        then(frontService).should(times(2)).getAnswer(any());
        then(frontService).shouldHaveNoMoreInteractions();
    }

    @Test
    void startTestingForOneTestSuite() {
        final var answer = new Answer("1");
        final var question = new Question(1, "question", answer);

        given(frontService.getStudent()).willReturn(new Student("ivan", "ivanov"));
        given(questionDao.getAllQuestion()).willReturn(Arrays.asList(question));

        new QuizServiceImpl(questionDao, frontService).startTesting();

        then(frontService).should().getStudent();
        then(questionDao).should().getAllQuestion();
        then(questionDao).shouldHaveNoMoreInteractions();
        then(frontService).should().getAnswer(any());
        then(frontService).should().printResult(any());
        then(frontService).shouldHaveNoMoreInteractions();
    }
}