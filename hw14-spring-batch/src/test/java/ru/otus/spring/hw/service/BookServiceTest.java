package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ru.otus.spring.hw.dao.AuthorRepositoryJdbc;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

public class BookServiceTest {

    @Test
    void givenBookWithOutAuthors_whenBookServiceAddAuthors_thenBookWithAuthors() {
        final var bookId = 1L;
        var book = new Book<Long>();
        book.setId(bookId);
        assertThat(book.getAuthors()).isEmpty();

        final var authorRepository = Mockito.mock(AuthorRepositoryJdbc.class);
        given(authorRepository.getByBookId(bookId)).willReturn(Collections.singletonList(new Author<Long>()));

        book = new BookService(authorRepository).addAuthorsToBook(book);

        then(authorRepository).should().getByBookId(bookId);
        assertThat(book.getAuthors()).isNotEmpty();
    }
}
