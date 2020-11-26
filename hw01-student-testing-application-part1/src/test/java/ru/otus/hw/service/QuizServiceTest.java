package ru.otus.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.service.QuizService;
import ru.otus.spring.hw.service.QuizServiceImpl;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @Mock
    private QuestionDao questionDao;

    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizServiceImpl(questionDao);
    }

    @Test
    void shouldReturnEmptyIfQuestionListIsEmpty() {
        given(questionDao.getQuestion(eq(0))).willReturn(Optional.empty());
        assertThat(quizService.getNextQuestion(null)).isEmpty();
    }

    @Test
    void checkingTheGettingOfTheFirstQuestion() {
        given(questionDao.getQuestion(eq(0))).willReturn(Optional.of(new Question(0)));
        assertThat(quizService.getNextQuestion(null)).isPresent();
    }

    @Test
    void checkingTheGettingOfTheNextFirstQuestion() {
        final int CURRENT_NUMBER = 1;
        final var question = new Question(CURRENT_NUMBER);
        given(questionDao.getQuestion(CURRENT_NUMBER + 1)).willReturn(Optional.of(new Question(CURRENT_NUMBER + 1)));
        assertThat(quizService.getNextQuestion(question)).isPresent();
        then(questionDao).should().getQuestion(CURRENT_NUMBER + 1);
    }

    @Test
    void shouldReturnEmptyIfQuestionIsLast() {
        final int LAST_NUMBER = 10;
        final var question = new Question(LAST_NUMBER);
        given(questionDao.getQuestion(LAST_NUMBER + 1)).willReturn(Optional.empty());
        assertThat(quizService.getNextQuestion(question)).isEmpty();
        then(questionDao).should().getQuestion(LAST_NUMBER + 1);
    }

    @Test
    void testCheckAnswer() {
        assertThatCode(() -> quizService.checkAnswer(null, null));
    }
}
