package ru.otus.spring.hw.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.otus.spring.hw.event.events.AbstractCustomEvent;

@SpringBootTest
class EventManagerImplTest {
    private boolean getEventConsumer1 = false;
    private boolean getEventConsumer2 = false;

    @Autowired
    private EventManager<AbstractCustomEvent> eventManager;

    @Autowired
    private EventPublisher<AbstractCustomEvent> eventPublisher;

    @BeforeEach
    void setUp() {
        getEventConsumer1 = false;
        getEventConsumer2 = false;
    }

    @Test
    void publishShouldInvokeConsumerMethod() {
        eventManager.connect(CustomEvent1.class, e -> getEventConsumer1 = true);

        eventPublisher.publish(new CustomEvent1(this, null));

        assertThat(getEventConsumer1).isTrue();
    }

    @Test
    void publishShouldInvokeOnlyClientConsumerMethod() {
        eventManager.connect(CustomEvent1.class, e -> getEventConsumer1 = true);
        eventManager.connect(CustomEvent2.class, e -> getEventConsumer2 = true);

        eventPublisher.publish(new CustomEvent2(this, null));

        assertThat(getEventConsumer1).isFalse();
        assertThat(getEventConsumer2).isTrue();
    }

    @Test
    void shouldDoesNotInvokeConsumerForUnknownEvent() {
        eventManager.connect(CustomEvent1.class, e -> getEventConsumer1 = true);

        eventPublisher.publish(new CustomEvent2(this, null));

        assertThat(getEventConsumer1).isFalse();
    }

    @Test
    void testEventWithPayload() {
        final var customSupplier = new EventClient();
        eventManager.connect(CustomEvent1.class, e -> customSupplier.setData((String) e.getPayload()));
        final String payload = "test data";

        eventPublisher.publish(new CustomEvent1(this, payload));

        assertThat(customSupplier.getData()).isEqualTo(payload);
    }
}
