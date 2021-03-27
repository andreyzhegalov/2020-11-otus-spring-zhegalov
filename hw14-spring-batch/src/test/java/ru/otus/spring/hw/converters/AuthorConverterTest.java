package ru.otus.spring.hw.converters;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.model.Author;

public class AuthorConverterTest {

    @Test
    void shouldReplaceLongIdToObjectId() {
        final var author = new Author<Long>();
        author.setId(1L);
        author.setName("name");

        final var convertedAuthor = AuthorConverter.convertId(author);
        assertThat(convertedAuthor.getId()).isExactlyInstanceOf(ObjectId.class);
        assertThat(convertedAuthor.getName()).isEqualTo(author.getName());
    }
}
