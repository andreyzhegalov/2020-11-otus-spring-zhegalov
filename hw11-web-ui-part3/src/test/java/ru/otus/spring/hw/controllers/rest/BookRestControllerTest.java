package ru.otus.spring.hw.controllers.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.ImmutableList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.service.BookService;

@WebMvcTest(controllers = BookRestController.class)
public class BookRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreService;

    @Test
    void shouldReturnBooksList() throws Exception {
        mvc.perform(get("/api/books")).andDo(print()).andExpect(status().isOk());
        then(bookService).should().findAll();
    }

    @Test
    void shouldNotSaveBookWithOutTitle() throws Exception {
        String bookJson = "{\"title\":\"\"}";

        mvc.perform(post("/api/books").content(bookJson).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue()))).andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isArray()).andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem("Please provide a book title")));

        then(bookService).shouldHaveNoInteractions();
    }

    @Test
    void shouldAddNewBookForExistedAuthorAndGenre() throws Exception {
        String bookJson = "{\"title\":\"new book title\", \"genreId\":\"genre id\", \"authorsId\":[\"author id1\", \"author id2\"]}";

        final var savedBook = new Book();
        savedBook.setTitle("new book title");
        savedBook.setId("123");
        savedBook.setGenre(new Genre("genre1"));
        savedBook.setAuthors(ImmutableList.of(new Author("name1"), new Author("name2")));

        given(bookService.save(any())).willReturn(new BookDto(savedBook));

        mvc.perform(post("/api/books").content(bookJson).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated());

        then(bookService).should().save(any(BookDto.class));
    }

    @Test
    void shouldReturnBadRequestWhenSaveBookServiceThrowException() throws Exception {
        String bookJson = "{\"title\":\"new book title\", \"genreId\":\"genre id\", \"authorsId\":[\"author id1\", \"author id2\"]}";
        // doThrow(new ServiceException("book service error
        // message")).when(bookService).save(any(BookDto.class));

        mvc.perform(post("/api/books").content(bookJson).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());

        then(bookService).should().save(any(BookDto.class));
    }

    @Test
    void shouldRemoveBook() throws Exception {
        final var bookId = "123";
        mvc.perform(delete("/api/books/{id}", bookId)).andDo(print()).andExpect(status().isOk());
        then(bookService).should().deleteBook(bookId);
    }
}
