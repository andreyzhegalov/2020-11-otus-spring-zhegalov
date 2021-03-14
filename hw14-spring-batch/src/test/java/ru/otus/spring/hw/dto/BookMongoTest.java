package ru.otus.spring.hw.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.model.Genre;

public class BookMongoTest {

    @Test
    void givenBookFromDb_whenCreateNewBookForMongo_thenIdIsTypeObjectId() {
        var book = new BookDb();
        book.setId(1L);
        final var genre = new Genre();
        genre.setId(2L);
        final var author = new AuthorDb();
        author.setId(3L);
        book.setGenre(genre);
        book.setAuthors(Collections.singletonList(author));

        final var bookWithObjectId = new BookMongo(book);

        assertThat(bookWithObjectId.getId()).isExactlyInstanceOf(ObjectId.class);
        // assertThat(bookWithObjectId.getGenre().getId()).isExactlyInstanceOf(ObjectId.class);
        assertThat(bookWithObjectId.getAuthors()).isNotEmpty()
                .allMatch(a -> a.getId().getClass().equals(ObjectId.class));
    }

}
