package ru.otus.spring.hw.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void shouldBeEqualOnlyById() {
        assertThat(new Author(1L, "name1")).isNotEqualTo(new Author(2L, "name2"));
        assertThat(new Author(1L, "name1")).isEqualTo(new Author(1L, "name2"));
    }

    @Test
    void hashCodeShouldBeConstant() {
        assertThat(new Author(1L, "name1")).hasSameHashCodeAs(new Author());
    }
}
