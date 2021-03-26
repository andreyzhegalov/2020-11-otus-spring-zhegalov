package ru.otus.spring.hw.config;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;
import ru.otus.spring.hw.model.Description;

@Configuration
public class FlowConfig {

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(1).get();
    }

    @MessagingGateway
    public interface MessageGateway {
        @Gateway(requestChannel = "coordinateToAddressFlow.input", replyChannel = "descriptionChannel")
        Description process(Coordinate coordinate);
    }

    @Bean
    public IntegrationFlow coordinateToAddressFlow() {
        return f->f.channel(c->c.queue(10))
            .handle("addressService", "getAddress", e->e.id("addressActivator"))
            .<Address, Boolean>route(p-> Objects.isNull(p),
                    mapping->mapping
                    .subFlowMapping(false,
                            sf->sf.handle("descriptionService", "getDescription", e->e.id("descriptionActivator"))
                                    .channel("descriptionChannel")
                        )
                    .channelMapping(true, "notFoundChannel")
                    );
    }
}
