package ru.otus.spring.hw.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldAddBookIdToModel() throws Exception {
        final var bookId = "123";
        mvc.perform(get("/comments/").param("bookId", bookId)).andDo(print())
                .andExpect(model().attributeExists("bookId")).andExpect(model().attribute("bookId", is(bookId)))
                .andExpect(view().name("comments"));
    }
}
