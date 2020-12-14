package ru.otus.spring.hw.service.front;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.service.IOLocalizedService;

@SpringBootTest
class FrontQuestionServiceImplTest {
    @MockBean
    private IOLocalizedService ioService;

    @Test
    void getAnswer() {
        final var question = new Question(1, "text", new Answer(""));

        new QuestionServiceImpl(ioService).getAnswer(question);

        then(ioService).should().print(anyString());
        then(ioService).should().read();
    }
}
