package ru.otus.spring.hw.controllers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@WebMvcTest(controllers = EditAuthorController.class)
public class EditAuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void shouldReturnEditAuthorView() throws Exception {
        mvc.perform(get("/edit_author")).andDo(print()).andExpect(model().attributeDoesNotExist("editAuthor"))
                .andExpect(status().isOk()).andExpect(view().name("edit_author"));
        then(authorRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldReturnFilledEditAuthorView() throws Exception {
        final var authorId = "123";
        mvc.perform(get("/edit_author/{id}", authorId)).andDo(print()).andExpect(model().attributeExists("authorDto"))
                .andExpect(status().isOk()).andExpect(view().name("edit_author"));
        then(authorRepository).should().findById(eq(authorId));
    }

    @Test
    void shouldNotSaveIfFieldsHasError() throws Exception {
        mvc.perform(post("/edit_author").param("name", " ")).andExpect(status().isOk())
                .andExpect(view().name("edit_author"));
        then(authorRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldSaveNewAuthor() throws Exception {
        final var newAuthorName = "new author name";
        mvc.perform(post("/edit_author").param("name", newAuthorName)).andExpect(status().isFound())
                .andExpect(view().name("redirect:/authors"));
        then(authorRepository).should().save(new Author(newAuthorName));
    }

    @Test
    void shouldUpdateAuthorWithId() throws Exception {
        final var updatedAuthorName = "updated author name";
        final var authorId = "123";
        mvc.perform(post("/edit_author").param("id", authorId).param("name", updatedAuthorName))
                .andExpect(status().isFound()).andExpect(view().name("redirect:/authors"));

        final var updatedAuthor = new Author(updatedAuthorName);
        updatedAuthor.setId(authorId);
        then(authorRepository).should().save(updatedAuthor);
    }
}
