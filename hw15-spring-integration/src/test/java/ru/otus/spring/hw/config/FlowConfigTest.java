package ru.otus.spring.hw.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.integration.test.mock.MockIntegration;
import org.springframework.messaging.MessageHandler;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;

@SpringBootTest
@SpringIntegrationTest()
public class FlowConfigTest {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private MockIntegrationContext mockIntegrationContext;

    @Test
    void givenNewMessageInCoordinateChannel_when_addressServiceFoundAddress_then_addressChannelHasNewAddressMessage() {
        mockIntegrationContext.substituteMessageHandlerFor("addressActivator",
                MockIntegration.mockMessageHandler().handleNextAndReply(m -> new Address()));

        final var messageArgumentCaptor = MockIntegration.messageArgumentCaptor();
        final MessageHandler mockHandler = Mockito.spy(MockIntegration.mockMessageHandler(messageArgumentCaptor))
                .handleNext(m -> {});
        mockIntegrationContext.substituteMessageHandlerFor("descriptionActivator", mockHandler);

        final var coordinateChannel =  this.context.getBean("coordinateToAddressFlow.channel#0", QueueChannel.class);
        assertThat(coordinateChannel).isNotNull();
        final var coordinateMessage = MessageBuilder.withPayload(new Coordinate()).build();

        coordinateChannel.send(coordinateMessage);

        then(mockHandler).should().handleMessage(any());;
        assertThat(messageArgumentCaptor.getValue()).isNotNull();
    }
}
