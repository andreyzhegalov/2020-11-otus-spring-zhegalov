package ru.otus.spring.hw.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.integration.test.context.SpringIntegrationTest;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;
import ru.otus.spring.hw.service.AddressService;

@SpringBootTest
@SpringIntegrationTest
public class MessageGatewayTest {

    @Autowired
    private MessageGateway messageGateway;

    @MockBean
    private AddressService addressService;

    @Test
    void shouldReturnAddressIfCoordinatesFound() {
        given(addressService.getAddress(any())).willReturn(new Address());
        assertThat(messageGateway.process(new Coordinate())).isInstanceOf(Address.class);
        then(addressService).should().getAddress(any());
    }
}
