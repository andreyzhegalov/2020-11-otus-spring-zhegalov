package ru.otus.spring.hw.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.Getter;
import lombok.Setter;
import ru.otus.spring.hw.event.events.CustomEvent;

@SpringBootTest
class EventManagerImplTest {
    private boolean getEventConsumer1 = false;
    private boolean getEventConsumer2 = false;

    @Autowired
    private EventManager<CustomEvent> eventManager;

    @Autowired
    private EventPublisher<CustomEvent> eventPublisher;

    private static class CustomEvent1 extends CustomEvent {
        public CustomEvent1(Object source, Object payload) {
            super(source, payload);
        }
    }

    private static class CustomEvent2 extends CustomEvent {
        public CustomEvent2(Object source, Object payload) {
            super(source, payload);
        }
    }

    @Test
    void publishShouldInvokeSupplierMethod() {
        getEventConsumer1 = false;
        eventManager.connect(CustomEvent1.class, e -> {
            getEventConsumer1 = true;
        });

        eventPublisher.publish(new CustomEvent1(this, null));

        assertThat(getEventConsumer1).isTrue();
    }

    @Test
    void publishShouldInvokeOnlyClientSupplierMethod() {
        getEventConsumer1 = false;
        getEventConsumer2 = false;

        eventManager.connect(CustomEvent1.class, e -> {
            getEventConsumer1 = true;
        });
        eventManager.connect(CustomEvent2.class, e -> {
            getEventConsumer2 = true;
        });

        eventPublisher.publish(new CustomEvent2(this, null));

        assertThat(getEventConsumer1).isFalse();
        assertThat(getEventConsumer2).isTrue();
    }

    private class EventClient {
        @Setter
        @Getter
        private String data;
    }

    @Test
    void testEventWithPayload() {
        final var customSupplier = new EventClient();

        eventManager.connect(CustomEvent1.class, e -> {
            customSupplier.setData((String) e.getPayload());
        });

        final String payload = "test data";
        eventPublisher.publish(new CustomEvent1(this, payload));
        assertThat(customSupplier.getData()).isEqualTo(payload);
    }

}
