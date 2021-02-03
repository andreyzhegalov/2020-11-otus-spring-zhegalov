package ru.otus.spring.hw.dto;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(bookDto.getAuthorsNameList()).hasSize(2);
        assertThat(bookDto.getAuthorsName()).isEqualTo("name1,name2");
    }
}
