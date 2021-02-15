package ru.otus.spring.hw.controllers.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.controllers.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

public class AuthorDtoTest {
    @Test
    void shouldCreateDtoFromAuthor() {
        final var author = new Author("id", "name", Arrays.asList(new Book("book1", null), new Book("book2", null)));
        final var authorDto = new AuthorDto(author);
        assertThat(authorDto.getId()).isEqualTo(author.getId());
        assertThat(authorDto.getName()).isEqualTo(author.getName());
        assertThat(authorDto.getBooksTitle()).containsExactlyInAnyOrder("book1", "book2");
    }

    @Test
    void shouldCreateAuthorFromDto() {
        final var authorDto = new AuthorDto();
        authorDto.setId("123");
        authorDto.setName("author name");
        final var author = authorDto.toEntity();
        assertThat(author.getId()).isEqualTo(authorDto.getId());
        assertThat(author.getName()).isEqualTo(authorDto.getName());
    }
}
