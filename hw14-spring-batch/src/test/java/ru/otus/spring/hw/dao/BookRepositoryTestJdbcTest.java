package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import ru.otus.spring.hw.model.Genre;

@JdbcTest
@ComponentScan(basePackages = "ru.otus.spring.hw.dao")

public class BookRepositoryTestJdbcTest {

    @Autowired
    private BookRepositoryJdbc bookRepository;

    @Test
    void shouldReturnAllBooks() {
        assertThat(bookRepository.findAll()).isNotNull().isNotEmpty();
    }

    @Test
    void bookShouldContainCorrectGenre() {
        final var bookList = bookRepository.findAll();
        assertThat(bookList).allMatch(b -> !b.getTitle().equals(""));
        bookList.forEach(b -> {
            assertThat(b.getGenre()).isNotNull().isInstanceOf(Genre.class);
            assertThat(b.getGenre().getId()).isNotNull();
            assertThat(b.getGenre().getName()).isNotBlank();
        });
    }

    @Test
    void bookShouldContainEmptyAuthorList() {
        assertThat(bookRepository.findAll()).allMatch(b -> b.getAuthors().isEmpty());
    }
}
