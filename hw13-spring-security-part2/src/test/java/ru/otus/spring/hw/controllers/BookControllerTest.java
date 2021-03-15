package ru.otus.spring.hw.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.ServiceException;

@WithMockUser(roles = "EDITOR")
@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreService;

    @Test
    void shouldRedirectFromRootToBooks() throws Exception {
        mvc.perform(get("/")).andExpect(view().name("redirect:/books"));
    }

    @Test
    void shouldReturnAllBooks() throws Exception {
        mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("authors")).andExpect(model().attributeExists("genres"))
                .andExpect(view().name("books"));
        then(bookService).should().findAll();
        then(authorRepository).should().findAll();
        then(genreService).should().findAll();
    }

    @Test
    @WithMockUser(roles = { "ADMIN", "USER" })
    void viewShouldNotContainsEditButtonForNotEditorUser() throws Exception {
        final var book = new Book("id", "title", new Genre("genreId", "name"));
        given(bookService.findAll()).willReturn(Collections.singletonList(book));

        final var result = mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk())
                .andExpect(model().attributeExists("books")).andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres")).andExpect(view().name("books")).andReturn();

        final String content = result.getResponse().getContentAsString();
        assertThat(content).isNotNull().doesNotContain("added-book-form");
        assertThat(content).doesNotContain("book-table-action-header");
        assertThat(content).doesNotContain("book-table-action-cell");
    }

    @Test
    @WithMockUser(roles = { "EDITOR" })
    void viewShouldContainsEditButtonForEditorUser() throws Exception {
        final var book = new Book("id", "title", new Genre("genreId", "name"));
        given(bookService.findAll()).willReturn(Collections.singletonList(book));
        final var result = mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk())
                .andExpect(model().attributeExists("books")).andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres")).andExpect(view().name("books")).andReturn();
        final String content = result.getResponse().getContentAsString();
        assertThat(content).isNotNull().contains("added-book-form");
        assertThat(content).contains("book-table-action-header");
        assertThat(content).contains("book-table-action-cell");
    }

    @Test
    @WithMockUser(roles = { "ADMIN", "EDITOR" })
    void viewShouldContainsAuthorAndGenrePageLinksForAnyNotUserRole() throws Exception {
        final var result = mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk())
                .andExpect(model().attributeExists("books")).andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres")).andExpect(view().name("books")).andReturn();
        final String content = result.getResponse().getContentAsString();
        assertThat(content).isNotNull().contains("goto-ref");
    }

    @Test
    @WithMockUser(roles = { "USER" })
    void viewShouldNotContainsAuthorAndGenrePageLinksForUserRole() throws Exception {
        final var result = mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk())
                .andExpect(model().attributeExists("books")).andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres")).andExpect(view().name("books")).andReturn();
        final String content = result.getResponse().getContentAsString();
        assertThat(content).isNotNull().doesNotContain("goto-ref");
    }

    @Test
    void shouldNotSaveBookWithEmptyTitle() throws Exception {
        mvc.perform(post("/books").param("title", " ").param("genreId", "genreId").param("authorsId", "authorsId"))
                .andDo(print()).andExpect(status().isBadRequest()).andExpect(content().string(not(emptyString())));
        then(bookService).shouldHaveNoInteractions();
    }

    @Test
    void shouldNotSaveBookWithOutTitle() throws Exception {
        mvc.perform(post("/books").param("genreId", "genreId").param("authorsId", "authorsId")).andDo(print())
                .andExpect(status().isBadRequest()).andExpect(content().string(not(emptyString())));

        then(bookService).shouldHaveNoInteractions();
    }

    @Test
    void shouldAddNewBookForExistedAuthorAndGenre() throws Exception {

        mvc.perform(post("/books").param("title", "book title").param("genreId", "genreId")
                .param("authorsId", "authorId").param("authorsId", "authorsId")).andDo(print())
                .andExpect(status().isFound()).andExpect(view().name("redirect:/books"));

        then(bookService).should().save(any(BookDto.class));
    }

    @Test
    void shouldReturnBadRequestWhenSaveBookServiceThrowException() throws Exception {
        doThrow(new ServiceException("error message")).when(bookService).save(any(BookDto.class));

        mvc.perform(
                post("/books").param("title", "book title").param("genreId", "genreId").param("authorsId", "authorsId"))
                .andDo(print()).andExpect(status().isBadRequest()).andExpect(content().string(not(emptyString())));

        then(bookService).should().save(any(BookDto.class));
    }

    @Test
    void shouldNotRemoveBookIfIdEmpty() throws Exception {
        mvc.perform(delete("/books").param("id", "")).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(not(emptyString())));
        then(bookService).shouldHaveNoInteractions();
    }

    @Test
    void shouldRemoveBook() throws Exception {
        final var bookId = "123";
        mvc.perform(delete("/books").param("id", bookId)).andDo(print()).andExpect(status().isFound())
                .andExpect(view().name("redirect:/books"));
        then(bookService).should().deleteBook(bookId);
    }
}
