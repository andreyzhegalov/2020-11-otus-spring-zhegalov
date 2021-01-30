package ru.otus.spring.hw.shell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.github.cloudyrock.spring.v5.MongockSpring5.MongockApplicationRunner;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;

import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.CommentService;
import ru.otus.spring.hw.service.IOAuthorService;
import ru.otus.spring.hw.service.IOBookService;
import ru.otus.spring.hw.service.IOCommentService;
import ru.otus.spring.hw.service.IOGenreService;

@SpringBootTest
class ApplicationCommandsTest {

    // Override mongock runner
    @MockBean
    private MongockApplicationRunner mongockApplicationRunner;

    @Autowired
    private Shell shell;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    @MockBean
    private IOBookService ioBookService;

    @MockBean
    private IOGenreService ioGenreService;

    @MockBean
    private IOAuthorService ioAuthorService;

    @MockBean
    private IOCommentService ioCommentService;

    @Captor
    private ArgumentCaptor<BookDto> bookDtoCaptor;

    @Test
    void printShouldPrintAllBooks() {
        shell.evaluate(() -> "pb");
        then(bookService).should().findAll();
        then(ioBookService).should().print(any());
    }

    @Test
    void shouldReadNewBook() {
        shell.evaluate(() -> "ab");
        then(ioBookService).should().getBook();
        then(bookService).should().save(any());
    }

    @Test
    void shouldPrintAllGenre() {
        shell.evaluate(() -> "pg");
        then(genreRepository).should().findAll();
        then(ioGenreService).should().print(any());
    }

    @Test
    void shouldPrintAllAuthors() {
        shell.evaluate(() -> "pa");
        then(authorRepository).should().findAll();
        then(ioAuthorService).should().print(any());
    }

    @Test
    void shouldDeleteBook() {
        final var id = "1";
        shell.evaluate(() -> "db " + id);
        then(bookService).should().deleteBook(eq(id));
    }

    @Test
    void shouldPrintAllComments() {
        shell.evaluate(() -> "pc");
        then(commentService).should().findAll();
        then(ioCommentService).should().print(any());
    }

    @Test
    void shouldAddNewCommentForBook() {
        final var bookId = "1";
        given(ioCommentService.getComment()).willReturn(new CommentDto("comment", bookId));
        shell.evaluate(() -> "add-comment");
        then(ioCommentService).should().getComment();
        then(commentService).should().addComment(any(CommentDto.class));
    }
}
