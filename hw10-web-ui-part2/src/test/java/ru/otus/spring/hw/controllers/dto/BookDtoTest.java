package ru.otus.spring.hw.controllers.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

public class BookDtoTest {
    @Test
    void shouldCreateDtoFromBook() {
        final var book = new Book("title", new Genre("genre"), new Author("name1"), new Author("name2"));
        book.getAuthors().get(0).setId("id1");
        book.getAuthors().get(1).setId("id2");
        final var bookDto = new BookDto(book);
        assertThat(bookDto.getId()).isEqualTo(book.getId());
        assertThat(bookDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookDto.getGenreName()).isEqualTo(book.getGenre().getName());
        assertThat(bookDto.getAuthorsName()).isEqualTo(Arrays.asList("name1", "name2"));
        assertThat(bookDto.getAuthorsId()).isEqualTo(Arrays.asList("id1", "id2"));
        assertThat(bookDto.getGenreId()).isEqualTo(book.getGenre().getId());
    }
}
