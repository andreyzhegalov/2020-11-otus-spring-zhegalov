package ru.otus.spring.hw.rest;

import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.dto.BookDtoInput;
import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.ServiceException;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Test
    void shouldReturnAllBooks() throws Exception {
        mvc.perform(get("/book")).andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("books"))
                .andExpect(view().name("book"));
        then(bookService).should().findAll();
    }

    @Test
    void shouldAddNewBookForExistedAuthorAndGenre() throws Exception {

        mvc.perform(post("/book").param("title", "book title").param("genreName", "book genre").param("authorsName",
                "name1 , name2")).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/book"));

        then(bookService).should().save(any(BookDtoInput.class));
    }

    @Test
    void shouldRetunBadRequesWhenSaveBookServiceThrowException() throws Exception {
        doThrow(ServiceException.class).when(bookService).save(any(BookDtoInput.class));

        mvc.perform(post("/book")).andDo(print()).andExpect(status().isBadRequest());

        then(bookService).should().save(any(BookDtoInput.class));
    }

    @Test
    void shouldRemoveBook() throws Exception {
        final var bookId = "123";
        mvc.perform(delete("/book").param("id", bookId)).andDo(print()).andExpect(status().isFound())
                .andExpect(view().name("redirect:/book"));
        then(bookService).should().deleteBook(bookId);
    }
}
