package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Arrays;
import java.util.Collections;
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
        final var authorId = "authorId";
        final var genreId = "genreId";

        final var newBookDto = new BookDto();
        newBookDto.setGenreId(genreId);
        newBookDto.setAuthorsId(Collections.singletonList(authorId));
        given(authorRepository.findById(authorId)).willReturn(Optional.empty());
        given(genreRepository.findById(genreId)).willReturn(Optional.of(new Genre()));

        assertThatCode(() -> bookService.save(newBookDto)).isInstanceOf(ServiceException.class);

        then(genreRepository).should().findById(genreId);
        then(authorRepository).should().findById(authorId);
    }

    @Test
    void shouldThrowExceptionIfGenreNotExistForUpdatedBook() {
        final var genreId = "genreId";
        final var authorId = "authorId";

        final var newBookDto = new BookDto();
        newBookDto.setGenreId(genreId);
        newBookDto.setAuthorsId(Collections.singletonList(authorId));

        given(authorRepository.findById(authorId)).willReturn(Optional.of(new Author()));
        given(genreRepository.findById(genreId)).willReturn(Optional.empty());

        assertThatCode(() -> bookService.save(newBookDto)).isInstanceOf(ServiceException.class);

        then(genreRepository).should().findById(genreId);
    }

    @Test
    void shouldSaveNewBookFromDto() {
        final var authorsId = Arrays.asList("id1", "id2");
        final var genreId = "genreId";

        final var newBookDto = new BookDto();
        newBookDto.setTitle("title");
        newBookDto.setGenreId(genreId);
        newBookDto.setAuthorsId(authorsId);

        final var author1 = new Author("name1");
        final var author2 = new Author("name2");

        given(authorRepository.findById("id1")).willReturn(Optional.of(author1));
        given(authorRepository.findById("id2")).willReturn(Optional.of(author2));
        given(genreRepository.findById(genreId)).willReturn(Optional.of(new Genre("genre")));

        bookService.save(newBookDto);

        then(authorRepository).should().findById("id1");
        then(authorRepository).should().findById("id2");
        then(genreRepository).should().findById(genreId);
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
