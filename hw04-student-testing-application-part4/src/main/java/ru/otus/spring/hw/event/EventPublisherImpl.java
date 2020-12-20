package ru.otus.spring.hw.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.spring.hw.event.events.AbstractCustomEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventPublisherImpl implements EventPublisher<AbstractCustomEvent> {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(AbstractCustomEvent event) {
        log.debug("publish message {}", event);
        applicationEventPublisher.publishEvent(event);
    }

}
