package ru.otus.spring.hw.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.then;
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

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

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
        mvc.perform(post("/api/authors").param("name", authorName)).andDo(print()).andExpect(status().isOk());

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
}
