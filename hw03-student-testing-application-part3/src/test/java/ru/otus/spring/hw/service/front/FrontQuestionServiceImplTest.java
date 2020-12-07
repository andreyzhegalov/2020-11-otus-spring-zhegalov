package ru.otus.spring.hw.service.front;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.service.IOService;

@ExtendWith(MockitoExtension.class)
class FrontQuestionServiceImplTest {
    @Mock
    private IOService ioService;

    @Test
    void getAnswer() {
        final var question = new Question(1, "text", new Answer(""));

        new QuestionServiceImpl(ioService).getAnswer(question);

        then(ioService).should().print(anyString());
        then(ioService).should().read();
    }
}
