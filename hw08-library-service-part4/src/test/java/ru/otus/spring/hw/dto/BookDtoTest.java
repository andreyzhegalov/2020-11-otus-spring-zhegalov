package ru.otus.spring.hw.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

class BookDtoTest {

    @Test
    void shouldMakeBookDtoFromBook() {
        final var book = new Book("1", "title", new Genre("1", "genre"));
        book.addAuthor(new Author("2", "name2"));
        book.addAuthor(new Author("3", "name3"));

        final var bookDto = new BookDto(book);

        assertThat(bookDto.getId()).isEqualTo(book.getId());
        assertThat(bookDto.getGenreId()).isEqualTo(book.getGenre().getId());
        assertThat(bookDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookDto.getAuthorIds()).containsExactlyInAnyOrderElementsOf(
                book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()));
    }
}
