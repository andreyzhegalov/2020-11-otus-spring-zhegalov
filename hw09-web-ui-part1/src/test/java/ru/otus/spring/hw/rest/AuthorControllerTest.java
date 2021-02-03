package ru.otus.spring.hw.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.RepositoryException;

@WebMvcTest(controllers = AuthorController.class)
public class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorRepository authorRepository;

    @Captor
    private ArgumentCaptor<Author> authorCaptor;

    @Test
    void shouldReturnAuthorList() throws Exception {
        mvc.perform(get("/authors")).andDo(print()).andExpect(status().isOk())
                .andExpect(model().attributeExists("authors")).andExpect(view().name("authors"));
        then(authorRepository).should().findAll();
    }

    @Test
    void shouldAddNewAuthor() throws Exception {
        final var authorName = "author name";
        mvc.perform(post("/authors").param("name", "author name")).andDo(print()).andExpect(status().isFound())
                .andExpect(view().name("redirect:/authors"));

        then(authorRepository).should().save(authorCaptor.capture());
        assertThat(authorCaptor.getValue().getName()).isEqualTo(authorName);
    }

    @Test
    void shouldRemoveAuthor() throws Exception {
        final var authorId = "123";
        mvc.perform(delete("/authors").param("id", authorId)).andDo(print()).andExpect(status().isFound())
                .andExpect(view().name("redirect:/authors"));
        then(authorRepository).should().deleteById(authorId);
    }

    @Test
    void shouldReturnErrorIfDeletedAuthorHasBook() throws Exception {
        final var authorId = "id_author_with_book";
        doThrow(RepositoryException.class).when(authorRepository).deleteById(authorId);

        mvc.perform(delete("/authors").param("id", authorId)).andDo(print()).andExpect(status().isBadRequest());

        then(authorRepository).should().deleteById(authorId);
    }
}
