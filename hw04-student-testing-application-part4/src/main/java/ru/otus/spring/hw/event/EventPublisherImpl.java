package ru.otus.spring.hw.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.spring.hw.event.events.CustomEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventPublisherImpl implements EventPublisher<CustomEvent> {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(CustomEvent event) {
        log.debug("publish message {}", event);
        applicationEventPublisher.publishEvent(event);
    }

}
