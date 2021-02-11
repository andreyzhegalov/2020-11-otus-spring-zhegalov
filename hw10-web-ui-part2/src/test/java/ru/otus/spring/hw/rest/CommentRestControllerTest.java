package ru.otus.spring.hw.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.service.CommentService;

@WebMvcTest(controllers = CommentRestController.class)
public class CommentRestControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CommentService commentService;

    @Test
    void shouldReturnAllComments() throws Exception {
        final var bookId = "123";
        mvc.perform(get("/api/comments").param("bookId", bookId)).andDo(print()).andExpect(status().isOk());
        then(commentService).should().findAllByBookId(bookId);
    }

    @Test
    void shouldSaveNewComment() throws Exception {
        final var commentJson = "{\"bookId\": \"123\", \"text\":\"comment text\"}";
        mvc.perform(post("/api/comments").content(commentJson).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated());
        then(commentService).should().addComment(any());
    }

    @Test
    void shouldNotSaveCommentWithoutText() throws Exception {
        final var commentJson = "{\"bookId\": \"123\", \"text\":\"\"}";
        mvc.perform(post("/api/comments").content(commentJson).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400))).andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Please provide a comment text")));
        then(commentService).shouldHaveNoInteractions();
    }

    @Test
    void shouldDeleteComment() throws Exception {
        final var commentId = "123";
        mvc.perform(delete("/api/comments/{id}", commentId)).andDo(print()).andExpect(status().isOk());
        then(commentService).should().deleteById(commentId);
    }

}
