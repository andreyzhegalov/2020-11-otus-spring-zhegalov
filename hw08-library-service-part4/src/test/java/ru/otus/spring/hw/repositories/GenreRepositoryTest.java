package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@EnableConfigurationProperties
@ComponentScan({ "ru.otus.spring.hw.repositories" })
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldReturnCorrectGenreList() {
        final var genres = genreRepository.findAll();
        assertThat(genres).hasSize(3);
    }
}
