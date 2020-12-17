package ru.otus.spring.hw.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.event.events.AbstractCustomEvent;
import ru.otus.spring.hw.event.events.ReportEvent;
import ru.otus.spring.hw.event.events.StartQuizEvent;
import ru.otus.spring.hw.service.QuizService;

@SpringBootTest
class StartQuizEventHandlerTest {
    @MockBean
    private EventPublisher<AbstractCustomEvent> eventPublisher;

    @MockBean
    private QuizService quizService;

    @Autowired
    private StartQuizEventHandler eventHandler;

    @Captor
    private ArgumentCaptor<AbstractCustomEvent> captor;

    @Test
    void shouldGetReportFromTheQuizServiceAndThenPublishReportEvent() {
        final var student = new Student("ivan", "ivanov");

        eventHandler.handle(new StartQuizEvent(this, student));

        then(quizService).should().startTesting(eq(student));
        then(eventPublisher).should().publish(captor.capture());
        assertThat(captor.getValue()).isInstanceOf(ReportEvent.class);
    }
}
