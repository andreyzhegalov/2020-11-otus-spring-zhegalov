package ru.otus.spring.hw.repositories;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import reactor.test.StepVerifier;
import ru.otus.spring.hw.event.GenreRepositoryListener;

@Import(GenreRepositoryListener.class)
public class GenreRepositoryTest extends AbstractRepositoryTest {

    private static final String USAGE_GENRE = "genre1";
    private static final String NOT_USAGE_GENRE = "genre3";
    private static final Duration TIMEOUT = Duration.ofMillis(100);

    @Autowired
    private GenreRepository genreRepository;

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    public void removalOfTheGenreWithTheBookShouldThrowException() {
        final var genre = genreRepository.findByName(USAGE_GENRE).blockOptional(TIMEOUT)
                .orElseGet(() -> fail("genre not exist"));
        StepVerifier.create(genreRepository.delete(genre)).expectError(RepositoryException.class).verify(TIMEOUT);
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    public void shouldRemoveGenre() {
        final var genre = genreRepository.findByName(NOT_USAGE_GENRE).blockOptional(TIMEOUT)
                .orElseGet(() -> fail("genre not exist"));
        StepVerifier.create(genreRepository.delete(genre)).expectComplete().verify(TIMEOUT);
    }
}
