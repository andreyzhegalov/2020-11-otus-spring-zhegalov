package ru.otus.spring.hw.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.dsl.context.IntegrationFlowContext.IntegrationFlowRegistration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.integration.test.mock.MockIntegration;
import org.springframework.messaging.MessageHandler;
import org.springframework.test.annotation.DirtiesContext;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;
import ru.otus.spring.hw.model.Description;

@SpringBootTest
@SpringIntegrationTest
@DirtiesContext
public class FlowConfigTest {
    private static final String COORDINATE_CHANNEL_NAME = "coordinateToDescriptionFlow.channel#1";
    private static final String ADDRESS_CHANNEL_NAME = "coordinateToDescriptionFlow.channel#2";

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MockIntegrationContext mockIntegrationContext;

    @Autowired
    private IntegrationFlowContext integrationFlowContext;

    @AfterEach
    public void tearDown() {
        this.mockIntegrationContext.resetBeans("addressActivator", "descriptionActivator");
    }

    @Test
    void givenNewMessageInCoordinateChannel_whenAddressServiceFoundAddress_thenDescriptionServiceRecivesAddressMessage() {
        final var coordinateChannel = this.context.getBean(COORDINATE_CHANNEL_NAME, DirectChannel.class);
        assertThat(coordinateChannel).isNotNull();

        final var coordinateMessage = MessageBuilder.withPayload(new Coordinate()).build();

        final var descriptionServiceArgumentCaptor = MockIntegration.messageArgumentCaptor();
        final MessageHandler mockDescriptionService = Mockito
                .spy(MockIntegration.mockMessageHandler(descriptionServiceArgumentCaptor)).handleNext(m -> {
                });
        mockIntegrationContext.substituteMessageHandlerFor("descriptionActivator", mockDescriptionService);

        // дано: сервис адреса находит адрес по координатам
        mockIntegrationContext.substituteMessageHandlerFor("addressActivator",
                MockIntegration.mockMessageHandler().handleNextAndReply(m -> Optional.of(new Address())));

        // когда: отправляем сообщение с координатами
        coordinateChannel.send(coordinateMessage);

        // тогда: сервис описания принимает сообщение с адресом
        then(mockDescriptionService).should().handleMessage(any());
        assertThat(descriptionServiceArgumentCaptor.getValue()).isNotNull();
    }

    @Test
    void givenNewAddressMessage_whenSendingNewAddressMessage_thenOutBoundEndPointReceivesNewMessage() {
        final var addressChannel = this.context.getBean(ADDRESS_CHANNEL_NAME, DirectChannel.class);
        assertThat(addressChannel).isNotNull();

        final var descriptionServiceArgumentCaptor = MockIntegration.messageArgumentCaptor();
        final MessageHandler outBoundHandler = Mockito
                .spy(MockIntegration.mockMessageHandler(descriptionServiceArgumentCaptor)).handleNext(m -> {
                });

        // добавляем обработчик на канал с описанием
        final var descriptionChannel = this.context.getBean("descriptionChannel", DirectChannel.class);
        StandardIntegrationFlow flow = IntegrationFlows.from(descriptionChannel).handle(outBoundHandler).get();
        IntegrationFlowRegistration registration = this.integrationFlowContext.registration(flow).register();

        // сервис описания находит по адресу описание и возвращает его
        mockIntegrationContext.substituteMessageHandlerFor("descriptionActivator",
                MockIntegration.mockMessageHandler().handleNextAndReply(address -> {
                    final var description = new Description();
                    description.setText("some description");
                    return description;
                }));

        // дано: сообщение с адресом
        final var addressMessageWithAddress = MessageBuilder.withPayload(Optional.of(new Address())).build();

        // когда: отправляем в канал адреса сообщение
        addressChannel.send(addressMessageWithAddress);

        // тогда: на вход обработчика описания приходит сообщение с описанием
        then(outBoundHandler).should().handleMessage(any());
        final var descriptionMessage = descriptionServiceArgumentCaptor.getValue();
        assertThat(descriptionMessage.getPayload()).isNotNull().isInstanceOf(Description.class);
        final var description = (Description) descriptionMessage.getPayload();
        assertThat(description.getText()).isEqualTo("some description");

        registration.destroy();
    }

    @Test
    void givenNewAddressMessageWithAddress_whenSendingAddressMessage_thenDescriptionServiceReceivesAddressMessage() {
        final var addressChannel = this.context.getBean(ADDRESS_CHANNEL_NAME, DirectChannel.class);
        assertThat(addressChannel).isNotNull();

        // подменяем обработчик описания c захватом параметров вызова
        final var descriptionServiceArgumentCaptor = MockIntegration.messageArgumentCaptor();
        final MessageHandler mockDescriptionService = Mockito
                .spy(MockIntegration.mockMessageHandler(descriptionServiceArgumentCaptor)).handleNext(m -> {
                });
        mockIntegrationContext.substituteMessageHandlerFor("descriptionActivator", mockDescriptionService);

        // дано: сообщение с адресом
        final var addressMessage = MessageBuilder.withPayload(Optional.of(new Address())).build();

        // когда: отправляем в канал адреса сообщение
        addressChannel.send(addressMessage);

        // тогда: на обработчик описания пришло сообщение с адресом
        then(mockDescriptionService).should().handleMessage(any());
        final var descriptionMessage = descriptionServiceArgumentCaptor.getValue();
        assertThat(descriptionMessage.getPayload()).isNotNull().isInstanceOf(Address.class);
    }

    @Test
    void givenNewCoordinateMessage_whenAddressNotFound_thenDescriptionMessageShouldBeReceivedWithEmptyText() {
        final var coordinateChannel = this.context.getBean(COORDINATE_CHANNEL_NAME, DirectChannel.class);
        assertThat(coordinateChannel).isNotNull();

        // сервис адреса НЕ находит адрес по координатам
        final MessageHandler mockAddressService = Mockito.spy(MockIntegration.mockMessageHandler())
                .handleNextAndReply(m -> Optional.empty());
        mockIntegrationContext.substituteMessageHandlerFor("addressActivator", mockAddressService);

        // подменяем сервис получения описания по адресу
        final MessageHandler mockDescriptionService = Mockito.spy(MockIntegration.mockMessageHandler());
        mockIntegrationContext.substituteMessageHandlerFor("descriptionActivator", mockDescriptionService);

        // добавляем мок для обработчика описания
        final var descriptionServiceArgumentCaptor = MockIntegration.messageArgumentCaptor();
        final MessageHandler outBoundHandler = Mockito
                .spy(MockIntegration.mockMessageHandler(descriptionServiceArgumentCaptor)).handleNext(m -> {
                });

        // добавляем обработчик на канал с описанием
        final var descriptionChannel = this.context.getBean("descriptionChannel", DirectChannel.class);
        StandardIntegrationFlow flow = IntegrationFlows.from(descriptionChannel).handle(outBoundHandler).get();
        IntegrationFlowRegistration registration = this.integrationFlowContext.registration(flow).register();

        // дано: новое сообщение с координатами
        final var coordinateMessage = MessageBuilder.withPayload(new Coordinate()).build();

        // когда: Отправляем сообщение с координатами
        coordinateChannel.send(coordinateMessage);

        // тогда: сервис описания не вызывается
        then(mockDescriptionService).should(never()).handleMessage(any());
        // тогда: получаем на выходе сообщение с пустым описанием
        then(outBoundHandler).should().handleMessage(any());
        final var descriptionMessage = descriptionServiceArgumentCaptor.getValue();
        assertThat(descriptionMessage.getPayload()).isNotNull().isInstanceOf(Description.class);
        final var description = (Description) descriptionMessage.getPayload();
        assertThat(description.getText()).isBlank();

        registration.destroy();
    }

}
