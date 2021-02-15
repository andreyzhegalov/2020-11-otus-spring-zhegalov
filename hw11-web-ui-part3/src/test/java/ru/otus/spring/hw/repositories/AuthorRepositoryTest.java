package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import ru.otus.spring.hw.event.AuthorMongoEventListener;

@Import(AuthorMongoEventListener.class)
public class AuthorRepositoryTest extends AbstractRepositoryTest {

    private static final String AUTHOR_WITHOUT_BOOK = "name3";
    private static final String AUTHOR_WITH_MANY_BOOKS = "name1";

    @Autowired
    private AuthorRepository authorRepository;

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void removalOfTheAuthorWithTheBookShouldThrowException() {
        final var author = authorRepository.findByName(AUTHOR_WITH_MANY_BOOKS)
                .orElseGet(() -> fail("author not exist"));

        assertThatCode(() -> authorRepository.delete(author)).isInstanceOf(RepositoryException.class);
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void removalOfTheAuthorWithOutTheBookShouldDeleteAuthor() {
        final var author = authorRepository.findByName(AUTHOR_WITHOUT_BOOK).orElseGet(() -> fail("author not found"));
        assertThat(author.getBooks()).isEmpty();

        assertThatCode(() -> authorRepository.delete(author)).doesNotThrowAnyException();

        assertThat(authorRepository.findByName(AUTHOR_WITHOUT_BOOK)).isEmpty();
    }
}
