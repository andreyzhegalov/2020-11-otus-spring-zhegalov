package ru.otus.spring.hw.converters;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.model.Genre;

public class GenreConverterTest {

    @Test
    void shouldReplaceLongIdToObjectId() {
        final var genre = new Genre<Long>();
        genre.setId(1L);
        genre.setName("name");

        final var convertedGenre = GenreConverter.convertId(genre);
        assertThat(convertedGenre.getId()).isExactlyInstanceOf(ObjectId.class);
        assertThat(convertedGenre.getName()).isEqualTo(genre.getName());
    }
}
