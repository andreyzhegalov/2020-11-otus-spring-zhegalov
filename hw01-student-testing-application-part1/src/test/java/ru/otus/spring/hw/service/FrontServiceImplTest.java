package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.front.Front;

@ExtendWith(MockitoExtension.class)
class FrontServiceImplTest {

    @Mock
    private Front front;

    @Mock
    private QuizService quizService;

    @Test
    void testNotPrintIfThereAreNoQuestions() {
        given(quizService.getNextQuestion(eq(null))).willReturn(Optional.empty());
        new FrontServiceImpl(quizService, front).printAllQuestion();
        then(front).shouldHaveNoInteractions();
    }

    @Test
    void printAllQuestion() {
        final var question1 = new Question(1, "text1", new Answer("answer1"));
        final var question2 = new Question(2, "text2", new Answer("answer2"));

        given(quizService.getNextQuestion(eq(null))).willReturn(Optional.of(question1));
        given(quizService.getNextQuestion(eq(question1))).willReturn(Optional.of(question2));

        new FrontServiceImpl(quizService, front).printAllQuestion();

        then(front).should().print(eq("text1"));
        then(front).should().print(eq("text2"));
    }
}
