package ru.otus.spring.hw.converters;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

public class BookConverterTest {

    @Test
    void shouldReplaceLongIdToObjectId() {
        var book = new Book<Long>();
        book.setId(1L);
        final var genre = new Genre<Long>();
        genre.setId(2L);
        final var author = new Author<Long>();
        author.setId(3L);
        book.setGenre(genre);
        book.setAuthors(Collections.singletonList(author));

        final var bookWithObjectId = BookConverter.convertId(book);

        assertThat(bookWithObjectId.getId()).isExactlyInstanceOf(ObjectId.class);
        assertThat(bookWithObjectId.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookWithObjectId.getGenre().getId()).isExactlyInstanceOf(ObjectId.class);
        assertThat(bookWithObjectId.getAuthors()).isNotEmpty();
        assertThat(bookWithObjectId.getAuthors().get(0).getId()).isExactlyInstanceOf(ObjectId.class);
    }
}
