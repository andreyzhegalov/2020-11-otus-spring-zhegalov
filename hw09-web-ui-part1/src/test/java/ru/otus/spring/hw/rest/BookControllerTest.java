package ru.otus.spring.hw.rest;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.ServiceException;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Test
    void shouldRedirectFromRootToBooks() throws Exception {
        mvc.perform(get("/")).andExpect(view().name("redirect:/books"));
    }

    @Test
    void shouldReturnAllBooks() throws Exception {
        mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("books"))
                .andExpect(view().name("books"));
        then(bookService).should().findAll();
    }

    @Test
    void shouldNotSaveBookWithEmptyTitle() throws Exception {
        mvc.perform(post("/books").param("title", " ").param("genreName", "book genre").param("authorsName",
                "name1 , name2")).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(not(emptyString())));
        then(bookService).shouldHaveNoInteractions();
    }

    @Test
    void shouldNotSaveBookWithOutTitle() throws Exception {
        mvc.perform(post("/books").param("genreName", "book genre").param("authorsName", "name1 , name2"))
                .andDo(print()).andExpect(status().isBadRequest()).andExpect(content().string(not(emptyString())));

        then(bookService).shouldHaveNoInteractions();
    }

    @Test
    void shouldAddNewBookForExistedAuthorAndGenre() throws Exception {

        mvc.perform(post("/books").param("title", "book title").param("genreName", "book genre").param("authorsName",
                "name1 , name2")).andDo(print()).andExpect(status().isFound())
                .andExpect(view().name("redirect:/books"));

        then(bookService).should().save(any(BookDto.class));
    }

    @Test
    void shouldReturnBadRequestWhenSaveBookServiceThrowException() throws Exception {
        doThrow(new ServiceException("error message")).when(bookService).save(any(BookDto.class));

        mvc.perform(post("/books").param("title", "book title").param("genreName", "book genre").param("authorsName",
                "name1 , name2")).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(not(emptyString())));

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
