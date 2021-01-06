package ru.otus.spring.hw.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void shouldBeEqualOnlyById() {
        assertThat(new Comment(1L, "comment1", new Book())).isNotEqualTo(new Comment(2L, "comment2", new Book()));
        assertThat(new Comment(1L, "comment1", new Book())).isEqualTo(new Comment(1L, "comment2", new Book()));
    }

    @Test
    void hashCodeShouldBeConstant() {
        assertThat(new Comment(1L, "comment1", new Book())).hasSameHashCodeAs(new Comment());
    }
}
