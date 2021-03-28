package ru.otus.spring.hw.controllers.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegratedActuatorTest {
    @Value("${webserver.address}")
    private String serverAddress;

    @Test
    void givenActuatorRequest_whenServerReceivedRequest_thenResponseContainsLinks() throws Exception {
        // Given
        final var request = new HttpGet(serverAddress + "/actuator");

        // When
        final var response = HttpClientBuilder.create().build().execute(request);

        // Then
        final var jsonFromResponse = EntityUtils.toString(response.getEntity());
        final var jsonNode = new ObjectMapper().readTree(jsonFromResponse);
        assertThat(jsonNode.get("_links")).isNotNull();
    }

    @Test
    void givenActuatorMetricsRequest_whenServerReceivedRequest_thenResponseContainsMetrics() throws Exception {
        // Given
        final var request = new HttpGet(serverAddress + "/actuator/metrics");

        // When
        final var response = HttpClientBuilder.create().build().execute(request);

        // Then
        final var jsonFromResponse = EntityUtils.toString(response.getEntity());
        final var jsonNode = new ObjectMapper().readTree(jsonFromResponse);
        assertThat(jsonNode.get("names")).isNotNull();
    }

}
