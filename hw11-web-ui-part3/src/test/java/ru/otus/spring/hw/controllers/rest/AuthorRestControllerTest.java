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

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.controllers.rest.AuthorRestController;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.RepositoryException;

@WebMvcTest(controllers = AuthorRestController.class)
public class AuthorRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorRepository authorRepository;

    @Captor
    private ArgumentCaptor<Author> authorCaptor;

    @Test
    void shouldReturnAuthorList() throws Exception {
        mvc.perform(get("/api/authors")).andDo(print()).andExpect(status().isOk());
        then(authorRepository).should().findAll();
    }

    @Test
    void shouldAddNewAuthor() throws Exception {
        final var authorName = "author name";
        String authorJson = "{\"name\":\"author name\"}";
        final var savedAuthor = new Author(authorName);
        savedAuthor.setId("132");
        given(authorRepository.save(any())).willReturn(savedAuthor);

        mvc.perform(
                post("/api/authors").content(authorJson).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated());

        then(authorRepository).should().save(authorCaptor.capture());
        assertThat(authorCaptor.getValue().getName()).isEqualTo(authorName);
    }

    @Test
    void shouldNotSaveAuthorWithEmptyName() throws Exception {

        String authorJson = "{\"name\":\"\"}";

        mvc.perform(
                post("/api/authors").content(authorJson).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue()))).andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isArray()).andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Please provide a author name")));

        then(authorRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldRemoveAuthor() throws Exception {
        final var authorId = "123";
        mvc.perform(delete("/api/authors/{id}", authorId)).andDo(print()).andExpect(status().isOk());
        then(authorRepository).should().deleteById(authorId);
    }

    @Test
    void shouldReturnErrorIfDeletedAuthorHasBook() throws Exception {
        final var authorId = "id_author_with_book";
        final var errorMessage = "error";
        doThrow(new RepositoryException(errorMessage)).when(authorRepository).deleteById(authorId);

        mvc.perform(delete("/api/authors/{id}", authorId)).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", is(errorMessage)));

        then(authorRepository).should().deleteById(authorId);
    }
}
