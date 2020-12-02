package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.dao.CsvQuestionDao;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @Mock
    private QuestionDao questionDao;

    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizServiceImpl(questionDao);
    }

    private Question makeQuestion(int number) {
        return new Question(number, "", new Answer(""));
    }

    @Test
    void shouldReturnEmptyIfQuestionListIsEmpty() {
        given(questionDao.getQuestion(CsvQuestionDao.getFirstQuestionNumber())).willReturn(Optional.empty());
        assertThat(quizService.getNextQuestion(null)).isEmpty();
    }

    @Test
    void checkingTheGettingOfTheFirstQuestion() {
        final int FIRST_QUESTION_NUMBER = CsvQuestionDao.getFirstQuestionNumber();
        given(questionDao.getQuestion(FIRST_QUESTION_NUMBER))
                .willReturn(Optional.of(makeQuestion(FIRST_QUESTION_NUMBER)));
        assertThat(quizService.getNextQuestion(null)).isPresent();
    }

    @Test
    void checkingTheGettingOfTheNextQuestion() {
        final int CURRENT_NUMBER = 1;
        final var question = makeQuestion(CURRENT_NUMBER);
        given(questionDao.getQuestion(CURRENT_NUMBER + 1)).willReturn(Optional.of(makeQuestion(CURRENT_NUMBER + 1)));
        assertThat(quizService.getNextQuestion(question)).isPresent();
    }

    @Test
    void shouldReturnEmptyIfQuestionIsLast() {
        final int LAST_NUMBER = 10;
        final var question = makeQuestion(LAST_NUMBER);
        given(questionDao.getQuestion(LAST_NUMBER + 1)).willReturn(Optional.empty());
        assertThat(quizService.getNextQuestion(question)).isEmpty();
    }

    @Test
    void testCheckAnswer() {
        assertThatThrownBy(() -> quizService.checkAnswer(null, null)).isInstanceOf(UnsupportedOperationException.class);
    }
}
