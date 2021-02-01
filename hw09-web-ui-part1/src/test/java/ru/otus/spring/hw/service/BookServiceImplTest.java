package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dto.BookDtoInput;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@SpringBootTest
public class BookServiceImplTest {

    @Import(BookServiceImpl.class)
    @Configuration
    public static class TestContext {
    }

    @Autowired
    private BookService bookService;

    @MockBean
    private CommentService commentService;

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
        then(commentService).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowExceptionIfAuthorNotExistForUpdatedBook() {
        final var genreName = "genre";
        final var authorName = "name";

        final var newBookDto = new BookDtoInput();
        newBookDto.setGenreName(genreName);
        newBookDto.setAuthorsName(authorName);
        given(authorRepository.findByName(authorName)).willReturn(Optional.empty());
        given(genreRepository.findByName(genreName)).willReturn(Optional.of(new Genre()));

        assertThatCode(() -> bookService.save(newBookDto)).isInstanceOf(ServiceException.class);

        then(genreRepository).should().findByName(genreName);
        then(authorRepository).should().findByName(authorName);
    }

    @Test
    void shouldThrowExceptionIfGenreNotExistForUpdatedBook() {
        final var genreName = "genre";
        final var authorName = "name";

        final var newBookDto = new BookDtoInput();
        newBookDto.setGenreName(genreName);
        newBookDto.setAuthorsName(authorName);
        given(authorRepository.findByName(authorName)).willReturn(Optional.of(new Author(authorName)));
        given(genreRepository.findByName(genreName)).willReturn(Optional.empty());

        assertThatCode(() -> bookService.save(newBookDto)).isInstanceOf(ServiceException.class);

        then(genreRepository).should().findByName(genreName);
    }

    @Test
    void shouldSaveNewBookFromDto() {
        final var authorsName = Arrays.asList("name1, name2");
        final var genreName = "genre3";

        final var newBookDto = new BookDtoInput();
        newBookDto.setTitle("title");
        newBookDto.setGenreName(genreName);
        newBookDto.setAuthorsName(String.join(",", authorsName));

        final var author1 = new Author("name1");
        final var author2 = new Author("name2");

        given(authorRepository.findByName("name1")).willReturn(Optional.of(author1));
        given(authorRepository.findByName("name2")).willReturn(Optional.of(author2));
        given(genreRepository.findByName(genreName)).willReturn(Optional.of(new Genre("genre")));

        bookService.save(newBookDto);

        then(authorRepository).should().findByName("name1");
        then(authorRepository).should().findByName("name2");
        then(genreRepository).should().findByName(genreName);
        then(bookRepository).should().save(bookCaptor.capture());

        final var savedBook = bookCaptor.getValue();
        assertThat(savedBook.getAuthors()).isNotNull().isNotEmpty();
        assertThat(savedBook.getGenre()).isInstanceOf(Genre.class);
    }

    @Test
    void deleteByIdShouldDeleteBook() {
        final var deletedBookId = "1";

        bookService.deleteBook(deletedBookId);

        then(bookRepository).should().deleteById(deletedBookId);
        then(authorRepository).shouldHaveNoInteractions();
        then(genreRepository).shouldHaveNoInteractions();
    }
}
