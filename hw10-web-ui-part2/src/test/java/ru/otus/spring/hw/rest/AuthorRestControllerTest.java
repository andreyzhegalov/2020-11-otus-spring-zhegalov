package ru.otus.spring.hw.rest;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.repositories.AuthorRepository;

@WebMvcTest(controllers = AuthorRestController.class)
public class AuthorRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void shouldReturnAuthorList() throws Exception {
        mvc.perform(get("/api/authors")).andDo(print()).andExpect(status().isOk());
        then(authorRepository).should().findAll();
    }
}
