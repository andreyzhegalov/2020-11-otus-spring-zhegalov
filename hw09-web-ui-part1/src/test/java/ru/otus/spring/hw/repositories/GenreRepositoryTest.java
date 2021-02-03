package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import ru.otus.spring.hw.event.GenreRepositoryListener;

@Import(GenreRepositoryListener.class)
public class GenreRepositoryTest extends AbstractRepositoryTest {

    private static final String USAGE_GENRE = "genre1";

    @Autowired
    private GenreRepository genreRepository;

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    public void removalOfTheGenreWithTheBookShouldThrowException() {
        final var genre = genreRepository.findByName(USAGE_GENRE).orElseGet(() -> fail("genre not exist"));

        assertThatCode(() -> genreRepository.delete(genre)).isInstanceOf(RepositoryException.class);
    }
}
