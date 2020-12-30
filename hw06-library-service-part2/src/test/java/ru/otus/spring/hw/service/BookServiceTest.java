package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@SpringBootTest
public class BookServiceTest {

    @Import(BookServiceImpl.class)
    @Configuration
    public static class TestContext {
    }

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    @Test
    void shouldReturnAllBooks() {
        bookService.findAll();

        then(bookRepository).should().findAll();
        then(authorRepository).shouldHaveNoInteractions();
        then(genreRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowExceptionIfAuthorNotExistForUpdatedBook() {
        final var authorId = 2L;
        final var bookDto = new BookDto(1L, "title", authorId, 3L);
        given(authorRepository.findById(authorId)).willReturn(Optional.empty());

        assertThatCode(() -> bookService.save(bookDto)).isInstanceOf(ServiceException.class);
        then(authorRepository).should().findById(authorId);
    }

    @Test
    void shouldThrowExceptionIfGenreNotExistForUpdatedBook() {
        final var authorId = 2L;
        final var genreId = 3L;
        final var bookDto = new BookDto(1L, "title", authorId, genreId);
        given(authorRepository.findById(authorId)).willReturn(Optional.of(new Author(authorId, "name")));
        given(genreRepository.findById(genreId)).willReturn(Optional.empty());

        assertThatCode(() -> bookService.save(bookDto)).isInstanceOf(ServiceException.class);

        then(genreRepository).should().findById(genreId);
    }

    @Test
    void shouldUpdateBook() {
        final var bookId = 1L;
        final var authorId = 2L;
        final var genreId = 3L;
        final var bookDto = new BookDto(bookId, "title", authorId, genreId);
        given(authorRepository.findById(authorId)).willReturn(Optional.of(new Author(authorId, "name")));
        given(genreRepository.findById(genreId)).willReturn(Optional.of(new Genre(genreId, "genre")));

        bookService.save(bookDto);

        then(authorRepository).should().findById(authorId);
        then(genreRepository).should().findById(genreId);
        then(bookRepository).should().save(bookCaptor.capture());

        final var updatedBook = bookCaptor.getValue();
        assertThat(updatedBook.getId()).isEqualTo(bookId);
        assertThat(updatedBook.getAuthors()).isNotNull().isNotEmpty();
        assertThat(updatedBook.getGenre()).isInstanceOf(Genre.class);
    }

    @Test
    void shouldSaveNewBook() {
        final var authorId = 2L;
        final var genreId = 3L;
        final var newBookDto = new BookDto("title", authorId, genreId);
        given(authorRepository.findById(authorId)).willReturn(Optional.of(new Author(authorId, "name")));
        given(genreRepository.findById(genreId)).willReturn(Optional.of(new Genre(genreId, "genre")));

        bookService.save(newBookDto);

        then(authorRepository).should().findById(authorId);
        then(genreRepository).should().findById(genreId);
        then(bookRepository).should().save(bookCaptor.capture());

        final var savedBook = bookCaptor.getValue();
        assertThat(savedBook.hasId()).isFalse();
        assertThat(savedBook.getAuthors()).isNotNull().isNotEmpty();
        assertThat(savedBook.getGenre()).isInstanceOf(Genre.class);
    }

    @Test
    void deleteByIdShouldDeleteBook() {
        final var deletedBookId = 1L;

        bookService.deleteBook(deletedBookId);

        then(bookRepository).should().remove(deletedBookId);
        then(authorRepository).shouldHaveNoInteractions();
        then(genreRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowExceptionIfBookNotExist() {
        final var bookId = 1L;
        final var comment = new Comment("new text");
        given(bookRepository.findById(eq(bookId))).willReturn(Optional.empty());

        assertThatCode(() -> bookService.addComment(bookId, comment)).isInstanceOf(ServiceException.class);

        then(bookRepository).should().findById(eq(bookId));
    }

    @Test
    void shouldSaveAddedCommentForExistedBook() {
        final var bookId = 1L;
        final var comment = new Comment("new text");
        final var book = new Book();
        final var initCommentCount = 0;
        given(bookRepository.findById(eq(bookId))).willReturn(Optional.of(book));

        bookService.addComment(bookId, comment);

        then(bookRepository).should().findById(eq(bookId));
        then(bookRepository).should().save(bookCaptor.capture());
        assertThat(bookCaptor.getValue()).isNotNull().extracting("comments.size").isEqualTo(initCommentCount + 1);
    }

    @Test
    void shouldAddAuthorToAuthorsList(){
    }

}
