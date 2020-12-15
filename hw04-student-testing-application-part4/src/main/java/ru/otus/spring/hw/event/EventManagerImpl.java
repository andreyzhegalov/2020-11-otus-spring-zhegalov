package ru.otus.spring.hw.event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;
import ru.otus.spring.hw.event.events.CustomEvent;

@Slf4j
public class EventManagerImpl implements EventManager<CustomEvent> {
    private final Map<Class<? extends CustomEvent>, Consumer<? super CustomEvent>> consumerMap;

    public EventManagerImpl() {
        this.consumerMap = new HashMap<>();
    }

    @Override
    public void connect(Class<? extends CustomEvent> eventType, Consumer<? super CustomEvent> consumer) {
        this.consumerMap.put(eventType, consumer);
    }

    @EventListener
    public void onNewEvent(CustomEvent newEvent) {
        log.debug("new event received {}", newEvent);
        if (!consumerMap.containsKey(newEvent.getClass())) {
            return;
        }
        final var consumer = consumerMap.get(newEvent.getClass());
        consumer.accept(newEvent);
    }
}
