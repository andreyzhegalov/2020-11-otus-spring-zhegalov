package ru.otus.spring.hw.controllers.page;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldRedirectFromRootToBooks() throws Exception {
        mvc.perform(get("/")).andExpect(view().name("redirect:/books.html"));
    }
}
