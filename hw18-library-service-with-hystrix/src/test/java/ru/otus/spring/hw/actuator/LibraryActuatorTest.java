package ru.otus.spring.hw.actuator;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = { "management.endpoint.library.enabled=true",
        "management.endpoints.web.exposure.include=library" })
@AutoConfigureMockMvc
public class LibraryActuatorTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturnBodyWithBookCountForAcrtuatorLibraryRequest() throws Exception {
        mvc.perform(get("/actuator/library")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.bookCnt", is(notNullValue())));
    }
}
