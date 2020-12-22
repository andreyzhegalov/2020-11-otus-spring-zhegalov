package ru.otus.spring.hw.shell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;

import ru.otus.spring.hw.dao.AuthorDao;
import ru.otus.spring.hw.dao.GenreDao;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.IOModelService;

@SpringBootTest
class ApplicationCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private BookService bookService;

    @MockBean
    private IOModelService<Book> ioBookService;

    @MockBean
    private GenreDao genreDao;

    @MockBean
    private AuthorDao authorDao;


    @MockBean
    private IOModelService<Genre> ioGenreService;

    @MockBean
    private IOModelService<Author> ioAuthorService;

    @Test
    void printShouldPrintAllBooks() {
        shell.evaluate(() -> "p");
        then(bookService).should().getAllBooks();
        then(ioBookService).should().print(any());
    }

    @Test
    void shouldReadNewBook() {
        shell.evaluate(() -> "a");
        then(ioBookService).should().get();
        then(bookService).should().saveBook(any());
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
}
