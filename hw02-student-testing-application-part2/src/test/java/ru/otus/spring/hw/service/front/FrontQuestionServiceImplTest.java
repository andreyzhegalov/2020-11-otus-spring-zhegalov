package ru.otus.spring.hw.service.front;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.service.IOService;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

@ExtendWith(MockitoExtension.class)
class FrontQuestionServiceImplTest {
    @Mock
    private IOService ioService;

    @Test
    void getAnswer() {
        final var question = new Question(1, "text", new Answer(""));

        final var answer = new FrontQuestionServiceImpl(ioService).getAnswer(question);
        assertThat(answer).isNotNull();

        then(ioService).should().print(question.getText());
        then(ioService).should().read();
    }
}
