package ru.otus.spring.hw.controllers.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegratedActuatorTest {
    @Value("${webserver.address}")
    private String serverAddress;

    @ParameterizedTest
    @ValueSource(strings = {"/actuator", "/actuator/metrics", "/actuator/health", "/actuator/logfile"})
    void givenActuatorRequest_whenServerReceivedRequest_thenResponseHasStatusOk(final String uri) throws Exception {
        // Given
        final var request = new HttpGet(serverAddress + uri);

        // When
        final var response = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }
}
