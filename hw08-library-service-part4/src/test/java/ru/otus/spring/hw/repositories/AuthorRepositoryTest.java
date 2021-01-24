package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import com.github.cloudyrock.spring.v5.MongockTestConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;

@MongockTestConfiguration
// @DataMongoTest(excludeAutoConfiguration =
// EmbeddedMongoAutoConfiguration.class)
@DataMongoTest()
@EnableConfigurationProperties
@ComponentScan({ "ru.otus.spring.hw.repositories" })
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldReturnCorrectAuthorWithBookList() {
        final var authors = authorRepository.findAll();
        assertThat(authors).isNotNull();
        assertThat(authors).isNotEmpty();
        final var author = authors.get(0);

        assertThat(author.getBooks()).isNotNull().allMatch(Objects::nonNull)
                .allMatch(b -> b.getId() != null && !b.getId().isEmpty());
    }

    @Test
    void shouldUpdateBooks() {
    }
}
