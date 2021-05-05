package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.mockito.internal.stubbing.answers.Returns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.controllers.dto.GenreDto;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.GenreRepository;

@SpringBootTest
public class GenreServiceCircuitBreakerTest {
    private final static int CIRCUIT_BREAKER_TIMEOUT = 500;

    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void shouldReturnEmptyListIfRepositoryNotResponse() throws InterruptedException {
        final var authorList = new Returns(List.of(new Genre()));
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, authorList);
        doAnswer(answersWithDelay).when(genreRepository).findAll();

        final var authorListFromCircuitBreaker = genreService.findAll();

        then(genreRepository).should().findAll();
        assertThat(authorListFromCircuitBreaker).isEmpty();
    }

    @Test
    void shouldReturnEmptyGenreWhenSaveIfRepositryNotResponse() {
        final var actualSavedGenre = new Genre();
        actualSavedGenre.setId("123");

        final var authorReturn = new Returns(actualSavedGenre);
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, authorReturn);
        doAnswer(answersWithDelay).when(genreRepository).save(any());

        final var savedGenreFromCircuitBreaker = genreService.saveGenre(new GenreDto(new Genre()));

        then(genreRepository).should().save(any());
        assertThat(savedGenreFromCircuitBreaker).isNotNull();
        assertThat(savedGenreFromCircuitBreaker.getId()).isNull();
    }

    @Test
    void shouldReturnFalseWhenDeleteGenreIfRepositoryNotResponse() {
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, null);
        doAnswer(answersWithDelay).when(genreRepository).deleteById(anyString());

        final var result = genreService.deleteGenre("123");
        then(genreRepository).should().deleteById(anyString());
        assertThat(result).isFalse();
    }

}
