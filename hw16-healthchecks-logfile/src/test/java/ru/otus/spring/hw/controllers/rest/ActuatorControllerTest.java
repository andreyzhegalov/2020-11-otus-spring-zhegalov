package ru.otus.spring.hw.controllers.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        // "management.endpoint.heartbeat.enabled=true",
        // "management.endpoints.web.exposure.include=heartbeat"
})
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ImportAutoConfiguration
public class ActuatorControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturnBodyWithLinksForActuatorRequest() throws Exception {
        mvc.perform(get("/actuator")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$._links", is(notNullValue())));
    }

    @Test
    void shouldReturnMetricsInTheForRequest() throws Exception{
        mvc.perform(get("/actuator/metrics")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$._links", is(notNullValue())));
    }
}
