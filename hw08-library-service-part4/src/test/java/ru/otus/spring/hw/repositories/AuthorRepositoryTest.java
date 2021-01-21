package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({ "ru.otus.spring.hw.repositories" })
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldReturnCorrectAuthorList() {
        final var authors = authorRepository.findAll();
        assertThat(authors).hasSize(3);
    }

}
