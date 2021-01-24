package ru.otus.spring.hw.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;

import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.model.Author;
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
    void shouldUpdateBook() {
        final var id = "3";
        given(ioBookService.getBook()).willReturn(new BookDto("title", "1"));
        shell.evaluate(() -> "ub " + id);
        then(ioBookService).should().getBook();
        then(bookService).should().save(bookDtoCaptor.capture());
        assertThat(bookDtoCaptor.getValue()).extracting("id").isEqualTo(id);
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

    @Test
    void shouldAddExistedAuthorToBook() {
        final var bookId = "1";
        final var addedAuthorId = "2";
        final var addedAuthor = new Author("");
        addedAuthor.setId(addedAuthorId);
        final var addedAuthorDto = new AuthorDto(addedAuthor);
        given(ioAuthorService.getAuthor()).willReturn(addedAuthorDto);
        given(authorRepository.findById(eq(addedAuthorId))).willReturn(Optional.of(addedAuthor));

        shell.evaluate(() -> "add-author " + bookId);

        then(ioAuthorService).should().getAuthor();
        then(bookService).should().addAuthor(eq(bookId), eq(addedAuthorDto));
    }

    @Test
    void shouldRemoveExistedAuthorFromBook() {
        final var bookId = "1";
        final var addedAuthorId = "2";
        final var addedAuthor = new Author("");
        addedAuthor.setId(addedAuthorId);
        final var addedAuthorDto = new AuthorDto(addedAuthor);
        given(ioAuthorService.getAuthor()).willReturn(addedAuthorDto);
        given(authorRepository.findById(eq(addedAuthorId))).willReturn(Optional.of(addedAuthor));

        shell.evaluate(() -> "delete-author " + bookId);

        then(ioAuthorService).should().getAuthor();
        then(bookService).should().removeAuthor(eq(bookId), eq(addedAuthorDto));
    }
}
