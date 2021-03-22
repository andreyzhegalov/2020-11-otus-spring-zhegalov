package ru.otus.spring.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

@Configuration
public class FlowConfig {

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).get();
    }

    @Bean
    public QueueChannel coordinateChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public PublishSubscribeChannel addressChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public IntegrationFlow messageFlow() {
        return IntegrationFlows.from("coordinateChannel")
            .handle("descriptionService", "getDescription")
                .channel("addressChannel")
                .get();
    }

}