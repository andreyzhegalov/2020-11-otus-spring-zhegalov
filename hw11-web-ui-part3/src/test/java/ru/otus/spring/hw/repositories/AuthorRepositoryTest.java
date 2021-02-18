package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import reactor.test.StepVerifier;
import ru.otus.spring.hw.event.AuthorMongoEventListener;

@Import(AuthorMongoEventListener.class)
public class AuthorRepositoryTest extends AbstractRepositoryTest {

    private static final String AUTHOR_WITHOUT_BOOK = "name3";
    private static final String AUTHOR_WITH_MANY_BOOKS = "name1";
    private static final Duration TIMEOUT = Duration.ofMillis(100);

    @Autowired
    private AuthorRepository authorRepository;

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void removalOfTheAuthorWithTheBookShouldThrowException() {
        final var author = authorRepository.findByName(AUTHOR_WITH_MANY_BOOKS).blockOptional(TIMEOUT)
                .orElseGet(() -> fail("author not exist"));
        StepVerifier.create(authorRepository.delete(author)).verifyError(RepositoryException.class);
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void removalOfTheAuthorWithOutTheBookShouldDeleteAuthor() {
        final var author = authorRepository.findByName(AUTHOR_WITHOUT_BOOK).blockOptional(TIMEOUT)
                .orElseGet(() -> fail("author not found"));
        assertThat(author.getBooks()).isEmpty();

        StepVerifier.create(authorRepository.delete(author)).expectComplete().verify(TIMEOUT);

        assertThat(authorRepository.findByName(AUTHOR_WITHOUT_BOOK).block(TIMEOUT)).isNull();
    }
}
