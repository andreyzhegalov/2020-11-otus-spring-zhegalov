package ru.otus.spring.hw.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.event.events.AbstractCustomEvent;
import ru.otus.spring.hw.event.events.LoggingEvent;
import ru.otus.spring.hw.event.events.UserLoggingEvent;
import ru.otus.spring.hw.service.front.UserService;

@RequiredArgsConstructor
@Component
public class LoginEventHandler {
    private final EventPublisher<AbstractCustomEvent> eventPublisher;
    private final UserService userService;

    @EventListener
    public void onLoggingEvent(LoggingEvent event) {
        final var newStudent = userService.getStudent();
        eventPublisher.publish(new UserLoggingEvent(this, newStudent));
    }
}
