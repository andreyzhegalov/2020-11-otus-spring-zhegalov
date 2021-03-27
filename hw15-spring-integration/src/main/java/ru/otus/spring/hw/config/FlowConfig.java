package ru.otus.spring.hw.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;
import ru.otus.spring.hw.model.Description;

@Configuration
public class FlowConfig {

    @MessagingGateway
    public interface MessageGateway {
        @Gateway(requestChannel = "coordinateToAddressFlow.input", replyChannel = "descriptionChannel")
        Description process(Coordinate coordinate);
    }

    @Bean
    public IntegrationFlow coordinateToAddressFlow() {
        return f->f.channel(c->c.queue(10))
            .bridge(e -> e.poller(Pollers.fixedDelay(100).maxMessagesPerPoll(1)))
            .handle("addressService", "getAddress", e->e.id("addressActivator"))
            .<Optional<Address>, Boolean>route(Optional::isEmpty,
                    mapping->mapping
                        .subFlowMapping(false,
                                sf->sf
                                .<Optional<Address>, Address>transform(Optional::get)
                                .handle(
                                    "descriptionService", "getDescription"
                                    , e->e.id("descriptionActivator"))
                                .channel("descriptionChannel")
                            )
                        .subFlowMapping(true,
                                sf->sf
                                .<Object, Description>transform(orderItem ->{
                                    System.out.println("transform !!!!!!!");
                                    return new Description();
                                })
                                .channel("descriptionChannel")
                        )
                );
    }
}
