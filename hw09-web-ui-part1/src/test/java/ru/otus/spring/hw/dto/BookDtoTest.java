package ru.otus.spring.hw.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

public class BookDtoTest {
    @Test
    void shouldCreateDtoFromBook() {
        final var book = new Book("title", new Genre("genre"), new Author("name1"), new Author("name2"));
        final var bookDto = new BookDto(book);
        assertThat(bookDto.getId()).isEqualTo(book.getId());
        assertThat(bookDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookDto.getGenreName()).isEqualTo(book.getGenre().getName());
        assertThat(bookDto.getAuthorsName()).isEqualTo(Arrays.asList("name1", "name2"));
    }
}
