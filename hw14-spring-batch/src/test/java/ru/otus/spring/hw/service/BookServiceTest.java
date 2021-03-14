package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ru.otus.spring.hw.dao.AuthorRepository;
import ru.otus.spring.hw.dto.AuthorDb;
import ru.otus.spring.hw.dto.BookDb;

public class BookServiceTest {

    @Test
    void givenBookWithOutAuthors_whenBookServiceAddAuthors_thenBookWithAuthors() {
        final var bookId = 1L;
        var book = new BookDb();
        book.setId(bookId);
        assertThat(book.getAuthors()).isEmpty();

        final var authorRepository = Mockito.mock(AuthorRepository.class);
        given(authorRepository.getByBookId(bookId)).willReturn(Collections.singletonList(new AuthorDb()));

        book = new BookService(authorRepository).addAuthors(book);

        then(authorRepository).should().getByBookId(bookId);
        assertThat(book.getAuthors()).isNotEmpty();
    }
}
