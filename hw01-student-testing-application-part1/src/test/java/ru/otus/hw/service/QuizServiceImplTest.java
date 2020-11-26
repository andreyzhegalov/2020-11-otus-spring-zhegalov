package ru.otus.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

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
public class QuizServiceImplTest {

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
    void checkGetFirstQuestion() {
        given(questionDao.getQuestion(eq(0))).willReturn(Optional.of(new Question()));
        assertThat(quizService.getNextQuestion(null)).isPresent();
    }

    @Test
    void testCheckAnswer() {
        assertThatCode(() -> quizService.checkAnswer(null, null));
    }
}
