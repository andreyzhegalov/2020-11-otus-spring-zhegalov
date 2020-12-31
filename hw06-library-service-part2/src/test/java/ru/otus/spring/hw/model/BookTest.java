package ru.otus.spring.hw.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void shouldBeEqualOnlyById() {
        assertThat(new Book(1L, "title1", new Author(), new Genre()))
                .isNotEqualTo(new Book(2L, "title2", new Author(), new Genre()));
        assertThat(new Book(1L, "title1", new Author(), new Genre()))
                .isEqualTo(new Book(1L, "title2", new Author(), new Genre()));
    }

    @Test
    void hashCodeShouldBeConstant() {
        assertThat(new Book(1L, "title1", new Author(), new Genre())).hasSameHashCodeAs(new Book());
    }
}
