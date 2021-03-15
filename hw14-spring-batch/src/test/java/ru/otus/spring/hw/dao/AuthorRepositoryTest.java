package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(AuthorRepositoryJdbc.class)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepositoryJdbc authorRepository;

    @Test
    void shouldReturnAuthorWithBookId() {
        final var authorList = authorRepository.getByBookId(1L);
        assertThat(authorList).isNotNull().hasSize(2);
        final var author = authorList.get(0);
        assertThat(author.getId()).isNotNull();
        assertThat(author.getName()).isNotBlank();
    }

    @Test
    void shouldReturnEmptyListForNotExistedBookId() {
        assertThat(authorRepository.getByBookId(0L)).isNotNull().isEmpty();
    }
}
