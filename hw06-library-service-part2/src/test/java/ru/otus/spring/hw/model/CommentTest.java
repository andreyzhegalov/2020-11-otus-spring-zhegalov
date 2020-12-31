package ru.otus.spring.hw.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void shouldBeEqualOnlyById() {
        assertThat(new Comment(1L, "comment1")).isNotEqualTo(new Comment(2L, "comment2"));
        assertThat(new Comment(1L, "comment1")).isEqualTo(new Comment(1L, "comment2"));
    }

    @Test
    void hashCodeShouldBeConstant() {
        assertThat(new Comment(1L, "comment1")).hasSameHashCodeAs(new Comment());
    }
}
