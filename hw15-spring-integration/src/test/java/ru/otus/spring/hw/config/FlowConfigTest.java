package ru.otus.spring.hw.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.integration.test.mock.MockIntegration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;
import ru.otus.spring.hw.service.DescriptionService;

@SpringBootTest
@SpringIntegrationTest()
public class FlowConfigTest {
    @Autowired
    private MockIntegrationContext mockIntegrationContext;

    @Autowired
    private QueueChannel coordinateChannel;

    @MockBean
    private DescriptionService descriptionService;



    @Test
    // givenNewMessageInCoordinateChannel_when_addressServiceFoundAddress_then_addressChannelHasNewAddressMessage
    void shouldInvokeAddressService() {

        mockIntegrationContext.substituteMessageHandlerFor("addressActivator",
                MockIntegration.mockMessageHandler().handleNextAndReply(m -> new Address()));

        final var messageArgumentCaptor = MockIntegration.messageArgumentCaptor();
        final MessageHandler mockHandler = Mockito.spy(MockIntegration.mockMessageHandler(messageArgumentCaptor))
                .handleNext(m -> {
                    System.out.println("!!!!!!!!!!!!!!!!!!!" + m);
                });
        mockIntegrationContext.substituteMessageHandlerFor("descriptionActivator", mockHandler);

        // final var mockCoordinateChannel = MockIntegration.mockMessageSource(new Coordinate());
        // mockIntegrationContext.substituteMessageSourceFor("addressActivator", mockCoordinateChannel);
//
        Message<Coordinate> coordinateMessage = MessageBuilder.withPayload(new Coordinate()).build();
        coordinateChannel.send(coordinateMessage);

        then(mockHandler).should().handleMessage(any());;

        assertThat(messageArgumentCaptor.getValue()).isNotNull();
    }
}
