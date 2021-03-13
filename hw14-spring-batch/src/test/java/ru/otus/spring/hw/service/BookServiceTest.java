package ru.otus.spring.hw.service;

import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Collections;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ru.otus.spring.hw.dao.AuthorRepository;
import ru.otus.spring.hw.model.AuthorDb;
import ru.otus.spring.hw.model.BookDb;
import ru.otus.spring.hw.model.Genre;

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

    @Test
    void givenBookWithLongId_whenBookServiceConvertId_thenAllIdMustBeObjectIdType() {
        var book = new BookDb();
        book.setId(1L);
        final var genre = new Genre();
        genre.setId(2L);
        final var author = new AuthorDb();
        author.setId(3L);
        book.setGenre(genre);
        book.setAuthors(Collections.singletonList(author));

        final var bookWithObjectId = new BookService(null).convertId(book);

        assertThat(bookWithObjectId.getId()).isExactlyInstanceOf(ObjectId.class);
        assertThat(bookWithObjectId.getGenre().getId()).isExactlyInstanceOf(ObjectId.class);
        assertThat(bookWithObjectId.getAuthors()).isNotEmpty()
                .allMatch(a -> a.getId().getClass().equals(ObjectId.class));
    }
}
