package ru.otus.spring.hw.service.front;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.service.IOLocalizedService;

@SpringBootTest
class QuestionServiceImplTest {

    @Import(QuestionServiceImpl.class)
    @Configuration
    public static class QuestionServiceInner {
    }

    @MockBean
    private IOLocalizedService ioService;

    @Autowired
    private QuestionService quizService;

    @Test
    void getAnswer() {
        final var question = new Question(1, "text", new Answer(""));

        quizService.getAnswer(question);

        then(ioService).should().print(anyString());
        then(ioService).should().read();
    }
}
