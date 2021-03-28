package ru.otus.spring.hw.controllers.rest;

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
    void shouldReturnListAllHealthChecks() throws Exception {
        mvc.perform(get("/actuator")).andDo(print()).andExpect(status().isOk());
    }
}
