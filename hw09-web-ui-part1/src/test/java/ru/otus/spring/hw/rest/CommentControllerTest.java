package ru.otus.spring.hw.rest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.service.CommentService;

@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @Test
    void shouldReturnAllBookComments() throws Exception {
        final var bookId = "123";
        final var commentsList = Collections.singletonList(new CommentDto());
        given(commentService.findAllByBookId(bookId)).willReturn(commentsList);
        mvc.perform(get("/comments").param("bookId", bookId)).andDo(print())
                .andExpect(model().attributeExists("comments")).andExpect(status().isOk())
                .andExpect(view().name("comments"));
        then(commentService).should().findAllByBookId(eq(bookId));
    }

}
