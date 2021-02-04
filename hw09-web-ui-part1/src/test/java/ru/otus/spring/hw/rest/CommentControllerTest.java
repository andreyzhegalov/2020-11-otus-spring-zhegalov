package ru.otus.spring.hw.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

    @Captor
    private ArgumentCaptor<CommentDto> captor;

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

    @Test
    void shouldNotSaveCommentWithEmptyText() throws Exception {
        mvc.perform(post("/comments").param("bookId", "123").param("text", "")).andDo(print())
                .andExpect(status().isBadRequest()).andExpect(content().string(not(emptyString())));
        then(commentService).shouldHaveNoInteractions();
    }

    @Test
    void shouldSaveBookComment() throws Exception {
        final var bookId = "123";
        final var text = "comment";
        mvc.perform(post("/comments").param("bookId", bookId).param("text", text)).andDo(print())
                .andExpect(status().isFound()).andExpect(view().name("redirect:/comments"));

        then(commentService).should().addComment(captor.capture());
        assertThat(captor.getValue().getBookId()).isEqualTo(bookId);
        assertThat(captor.getValue().getText()).isEqualTo(text);
    }

    @Test
    void shouldNotRemoveCommentIfIdEmpty() throws Exception {
        mvc.perform(delete("/comments").param("id", "").param("bookId", "123")).andDo(print())
                .andExpect(status().isBadRequest()).andExpect(content().string(not(emptyString())));
        then(commentService).shouldHaveNoInteractions();
    }

    @Test
    void shouldDeleteBookComment() throws Exception {
        final var commentId = "comment_id";
        final var bookId = "book_id";
        mvc.perform(delete("/comments").param("id", commentId).param("bookId", bookId)).andDo(print())
                .andExpect(status().isFound()).andExpect(view().name("redirect:/comments"));

        then(commentService).should().deleteById(eq(commentId));
    }

}
