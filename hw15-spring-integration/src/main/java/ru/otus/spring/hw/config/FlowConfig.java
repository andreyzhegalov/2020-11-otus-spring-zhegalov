package ru.otus.spring.hw.config;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;

import ru.otus.spring.hw.model.Address;

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
    public MessageChannel addressChannel() {
        return MessageChannels.publishSubscribe().get();
        // return MessageChannels.queue(10).get();
    }

    @Bean
    public IntegrationFlow coordinateToAddressFlow() {
        return IntegrationFlows.from("coordinateChannel")
            .handle("addressService", "getAddress", e->e.id("addressActivator"))
            .<Address, Boolean>route(p-> Objects.isNull(p),
                    mapping->mapping
                    .channelMapping(true, "notFoundChannel")
                    .channelMapping(false, "addressToDescriptionFlow.input")
                    )
            .get();
    }

    @Bean
    public IntegrationFlow addressToDescriptionFlow(){
        return f->f
            .handle("descriptionService", "getDescription", e->e.id("descriptionActivator"))
            .channel("descriptionChannel");
    }

}
