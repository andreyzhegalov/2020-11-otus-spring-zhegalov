package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.mockito.internal.stubbing.answers.Returns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@SpringBootTest
public class BookServiceCircuitBreaker {
    private final static int CIRCUIT_BREAKER_TIMEOUT = 500;

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

    @Test
    void shouldReturnEmptyListIfRepositoryNotResponse() {
        final var savedBookList = new Returns(List.of(new Book()));
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, savedBookList);
        doAnswer(answersWithDelay).when(bookRepository).findAll();

        final var bookList = bookService.findAll();

        then(bookRepository).should().findAll();
        assertThat(bookList).isEmpty();
    }

    @Test
    void shouldNotSaveBookIfRepositoryNotResponse() {
        final var newBookDto = new BookDto();
        newBookDto.setTitle("title");
        newBookDto.setGenreId("genreId");
        newBookDto.setAuthorsId(List.of("id1"));
        final var author1 = new Author("name1");
        final var book = new Book();
        book.setId("123");
        book.setGenre(new Genre());
        given(authorRepository.findById(anyString())).willReturn(Optional.of(author1));
        given(genreRepository.findById(anyString())).willReturn(Optional.of(new Genre("genre")));

        final var savedBook = new Returns(book);
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, savedBook);
        doAnswer(answersWithDelay).when(bookRepository).save(any());

        final var bookAfterSaved = bookService.save(newBookDto);

        then(bookRepository).should().save(any());
        assertThat(bookAfterSaved).isNotNull();
        assertThat(bookAfterSaved.getId()).isNull();
    }

    @Test
    void shouldReturnFalseWhenDeleteBookIfRepositoryNotResponse(){
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, null);
        doAnswer(answersWithDelay).when(bookRepository).deleteById(anyString());

        final var result = bookService.deleteBook("123");
        then(bookRepository).should().deleteById(anyString());
        assertThat(result).isFalse();
    }

}
