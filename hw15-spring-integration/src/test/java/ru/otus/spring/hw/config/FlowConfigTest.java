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
import org.springframework.integration.channel.QueueChannel;
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
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;
import ru.otus.spring.hw.model.Description;

@SpringBootTest
@SpringIntegrationTest()
@DirtiesContext
public class FlowConfigTest {
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
   void givenNewMessageInCoordinateChannel_whenAddressServiceFoundAddress_thenAddressChannelHasNewAddressMessage() {
       // дано: сервис адреса находит адрес по координатам
       mockIntegrationContext.substituteMessageHandlerFor("addressActivator",
               MockIntegration.mockMessageHandler().handleNextAndReply(m -> Optional.of(new Address())));

       // дано: подменяем сервис получения описания с захватом аргумента
       final var messageArgumentCaptor = MockIntegration.messageArgumentCaptor();
       final MessageHandler descriptionService = Mockito.spy(MockIntegration.mockMessageHandler(messageArgumentCaptor))
               .handleNext(m -> {
               });
       mockIntegrationContext.substituteMessageHandlerFor("descriptionActivator", descriptionService);

       // когда: Отправляем сообщение с координатами
       final var coordinateChannel = this.context.getBean("coordinateToAddressFlow.channel#1", DirectChannel.class);
       assertThat(coordinateChannel).isNotNull();
       final var coordinateMessage = MessageBuilder.withPayload(new Coordinate()).build();
       coordinateChannel.send(coordinateMessage);

       // тогда: сервис получения описания принимает сообщение с адресом
       then(descriptionService).should().handleMessage(any());
       assertThat(messageArgumentCaptor.getValue()).isNotNull();
   }

    @Test
    void givenNewAddressMessage_whenDescriptionServiceFoundDescription_thenDescriptionMessageHasData() {
        // дано: подменяем обработчик описания c захватом параметров вызова
        final var messageArgumentCaptor = MockIntegration.messageArgumentCaptor();
        final MessageHandler mockHandler = Mockito.spy(MockIntegration.mockMessageHandler(messageArgumentCaptor))
                .handleNext(m -> {
                });
        mockIntegrationContext.substituteMessageHandlerFor("descriptionActivator", mockHandler);

        // когда: отправляем в канал координат сообщение
        final var addressChannel = this
            .context.getBean("coordinateToAddressFlow.channel#2", DirectChannel.class);
        assertThat(addressChannel).isNotNull();
        final var addressMessage = MessageBuilder.withPayload(Optional.of(new Address())).build();
        addressChannel.send(addressMessage);

        // тогда: проверяем что на обработчик описания пришло сообщение с адресом
        then(mockHandler).should().handleMessage(any());
        final var descriptionMessage = messageArgumentCaptor.getValue();
        assertThat(descriptionMessage.getPayload()).isNotNull().isInstanceOf(Address.class);
    }

    @Test
    void givenNewAddressMessage_whenSendingNewAddressMessage_thenOutBoundEndPointReceiveNewMessage() {
        // дано: мок для обработчика описания
        final var messageArgumentCaptor = MockIntegration.messageArgumentCaptor();
        final MessageHandler mockHandler = Mockito.spy(MockIntegration.mockMessageHandler(messageArgumentCaptor))
                .handleNext(m -> {
                });

        // дано: добавляем мок обработчик на канал описания
        final var descriptionChannel = this.context.getBean("descriptionChannel", DirectChannel.class);
        StandardIntegrationFlow flow = IntegrationFlows.from(descriptionChannel).handle(mockHandler).get();
        IntegrationFlowRegistration registration = this.integrationFlowContext.registration(flow).register();

        // когда: отправляем в канал адреса сообщение
        final var addressChannel = this.context.getBean("coordinateToAddressFlow.channel#2", DirectChannel.class);
        assertThat(addressChannel).isNotNull();
        final var addressMessage = MessageBuilder.withPayload(Optional.of(new Address())).build();
        addressChannel.send(addressMessage);

        // тогда: на вход обработчика описания приходит сообщение с описанием
        then(mockHandler).should().handleMessage(any());
        final var descriptionMessage = messageArgumentCaptor.getValue();
        assertThat(descriptionMessage.getPayload()).isNotNull().isInstanceOf(Description.class);

        registration.destroy();
    }

    @Test
    void givenNewCoordinateMessage_whenAddressNotFound_thenDescriptionMessageShouldBeReceived() {
        // дано: сервис адреса НЕ находит адрес по координатам
        final MessageHandler mockAddressService = Mockito.spy(MockIntegration.mockMessageHandler())
                .handleNextAndReply(m -> Optional.empty());
        mockIntegrationContext.substituteMessageHandlerFor("addressActivator", mockAddressService);

        // дано: подменяем сервис получения описания по адресу
        final MessageHandler mockDescriptionService = Mockito.spy(MockIntegration.mockMessageHandler()
                .handleNext(m->{
                }));
        mockIntegrationContext.substituteMessageHandlerFor("descriptionActivator", mockDescriptionService);

        // дано: добавляем мок для обработчика описания
        final var messageArgumentCaptor = MockIntegration.messageArgumentCaptor();
        final MessageHandler mockClientHandler = Mockito
            .spy(MockIntegration.mockMessageHandler(messageArgumentCaptor))
                .handleNext(m -> {
                });

        // дано: добавляем мок обработчик на канал описания
        final var descriptionChannel = this.context.getBean("descriptionChannel", DirectChannel.class);
        assertThat(descriptionChannel).isNotNull();
        StandardIntegrationFlow flow = IntegrationFlows
            .from(descriptionChannel).handle(mockClientHandler).get();
        IntegrationFlowRegistration registration = this.integrationFlowContext.registration(flow).register();

        // когда: Отправляем сообщение с координатами
        final var coordinateChannel = this
            .context.getBean("coordinateToAddressFlow.channel#1", DirectChannel.class);
        assertThat(coordinateChannel).isNotNull();
        final var coordinateMessage = MessageBuilder.withPayload(new Coordinate()).build();
        coordinateChannel.send(coordinateMessage);

        // тогда: сервис описания не вызывается
        then(mockDescriptionService).should(never()).handleMessage(any());
        then(mockClientHandler).should().handleMessage(any());
        // тогда: получаем сообщение с пустым описанием
        final var descriptionMessage = messageArgumentCaptor.getValue();
        assertThat(descriptionMessage.getPayload()).isNotNull().isInstanceOf(Description.class);

        registration.destroy();
    }

}
