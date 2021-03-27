package ru.otus.spring.hw.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.config.FlowConfig.MessageGateway;
import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;
import ru.otus.spring.hw.model.Description;
import ru.otus.spring.hw.service.AddressService;
import ru.otus.spring.hw.service.DescriptionService;

@SpringBootTest
public class MessageGatewayTest {

    @Autowired
    private MessageGateway messageGateway;

    @MockBean
    private AddressService addressService;

    @MockBean
    private DescriptionService descriptionService;


    @Test
    void shouldGetAddressFromAddressService() {
        given(addressService.getAddress(any())).willReturn(Optional.of(new Address()));
        given(descriptionService.getDescription(any())).willReturn(new Description());

        assertThat(messageGateway.process(new Coordinate())).isNotNull();

        then(addressService).should().getAddress(any());
        then(descriptionService).should().getDescription(any());
    }
}
