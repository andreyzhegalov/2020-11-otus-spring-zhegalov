package ru.otus.spring.hw.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.event.events.AbstractCustomEvent;
import ru.otus.spring.hw.event.events.LoggingEvent;
import ru.otus.spring.hw.event.events.UserLoggingEvent;
import ru.otus.spring.hw.service.front.UserService;

@SpringBootTest
class LoginEventHandlerTest {
    @MockBean
    private EventPublisher<AbstractCustomEvent> eventPublisher;

    @MockBean
    private UserService userService;

    @Autowired
    private LoginEventHandler eventHandler;

    @Captor
    private ArgumentCaptor<AbstractCustomEvent> captor;

    @Test
    void shouldGetNewUserFromTheUserServiceAndThenPublishUserLoggingEvent() {
        eventHandler.handle(new LoggingEvent(this));

        then(userService).should().getStudent();
        then(eventPublisher).should().publish(captor.capture());
        assertThat(captor.getValue()).isInstanceOf(UserLoggingEvent.class);
    }
}
