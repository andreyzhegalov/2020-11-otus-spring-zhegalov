package ru.otus.spring.hw.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;

import ru.otus.spring.hw.dao.AuthorDao;
import ru.otus.spring.hw.dao.BookDao;
import ru.otus.spring.hw.dao.GenreDao;
import ru.otus.spring.hw.dao.dto.BookDto;
import ru.otus.spring.hw.service.io.IOAuthorService;
import ru.otus.spring.hw.service.io.IOBookService;
import ru.otus.spring.hw.service.io.IOGenreService;

@SpringBootTest
class ApplicationCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private IOBookService ioBookService;

    @MockBean
    private GenreDao genreDao;

    @MockBean
    private AuthorDao authorDao;

    @MockBean
    private BookDao bookDao;

    @MockBean
    private IOGenreService ioGenreService;

    @MockBean
    private IOAuthorService ioAuthorService;

    @Captor
    private ArgumentCaptor<BookDto> bookDtoCaptor;

    @Test
    void printShouldPrintAllBooks() {
        shell.evaluate(() -> "pb");
        then(bookDao).should().getAll();
        then(ioBookService).should().print(any());
    }

    @Test
    void shouldReadNewBook() {
        shell.evaluate(() -> "ab");
        then(ioBookService).should().get();
        then(bookDao).should().insertBook(any());
    }

    @Test
    void shouldPrintAllGenre() {
        shell.evaluate(() -> "pg");
        then(genreDao).should().getAll();
        then(ioGenreService).should().print(any());
    }

    @Test
    void shouldPrintAllAuthors() {
        shell.evaluate(() -> "pa");
        then(authorDao).should().getAll();
        then(ioAuthorService).should().print(any());
    }

    @Test
    void shouldDeleteBook() {
        final var id = 1L;
        shell.evaluate(() -> "db " + id);
        then(bookDao).should().deleteBook(eq(id));
    }

    @Test
    void shouldUpdateBook() {
        final long id = 3L;
        given(ioBookService.get()).willReturn(new BookDto("title", 1L, 1L));
        shell.evaluate(() -> "ub " + id);
        then(ioBookService).should().get();
        then(bookDao).should().updateBook(bookDtoCaptor.capture());
        assertThat(bookDtoCaptor.getValue()).extracting("id").isEqualTo(id);
    }
}
