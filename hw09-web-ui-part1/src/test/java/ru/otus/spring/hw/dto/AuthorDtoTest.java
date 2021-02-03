package ru.otus.spring.hw.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

public class AuthorDtoTest {
    @Test
    void shouldCreateDtoFromAuthor() {
        final var author = new Author("id", "name", Arrays.asList(new Book("book1", null), new Book("book2", null)));
        final var authorDto = new AuthorDto(author);
        assertThat(authorDto.getId()).isEqualTo(author.getId());
        assertThat(authorDto.getName()).isEqualTo(author.getName());
        assertThat(authorDto.getBooksTitle()).isEqualTo("book1,book2");
    }

}
