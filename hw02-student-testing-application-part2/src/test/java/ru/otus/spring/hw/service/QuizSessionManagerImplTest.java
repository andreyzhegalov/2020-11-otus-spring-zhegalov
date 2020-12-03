package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Student;

@ExtendWith(MockitoExtension.class)
class QuizSessionManagerImplTest {

    @Mock
    private FrontService frontService;

    @Mock
    private QuizService quizService;

    @Mock
    private QuestionDao questionDao;

    @Test
    void startTestingForOneTestSuite() {
        final var answer = new Answer("1");
        final var question = new Question(1, "question", answer);

        given(frontService.getStudent()).willReturn(new Student("ivan", "ivanov"));
        given(quizService.getNextQuestion(null)).willReturn(Optional.of(question));
        given(frontService.getAnswer(eq(question))).willReturn(answer);
        given(quizService.getNextQuestion(question)).willReturn(Optional.empty());

        assertThat(new QuizSessionManagerImpl(questionDao, frontService, quizService)).isNotNull();
        new QuizSessionManagerImpl(questionDao, frontService, quizService).startTesting();

        then(frontService).should().getStudent();
        then(quizService).should().getNextQuestion(eq(null));
        then(frontService).should().getAnswer(question);
        then(quizService).should().getNextQuestion(question);
        then(quizService).shouldHaveNoMoreInteractions();
        then(frontService).should().printResult(any());
        then(frontService).shouldHaveNoMoreInteractions();
    }

    @Test
    void startTestingForOneTestSuite1() {
        final var answer = new Answer("1");
        final var question = new Question(1, "question", answer);

        given(frontService.getStudent()).willReturn(new Student("ivan", "ivanov"));
        given(questionDao.getAllQuestion()).willReturn(Arrays.asList(question));

        new QuizSessionManagerImpl(questionDao, frontService, quizService).startTesting1();

        then(frontService).should().getStudent();
        then(questionDao).should().getAllQuestion();
        then(questionDao).shouldHaveNoMoreInteractions();
        then(frontService).should().getAnswer(any());
        then(frontService).should().printResult(any());
        then(frontService).shouldHaveNoMoreInteractions();
    }
}
