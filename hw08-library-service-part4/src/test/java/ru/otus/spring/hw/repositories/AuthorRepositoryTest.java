package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import ru.otus.spring.hw.event.AuthorMongoEventListener;
import ru.otus.spring.hw.model.Book;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@EnableConfigurationProperties
@ComponentScan({ "ru.otus.spring.hw.repositories" })
@Import(AuthorMongoEventListener.class)
public class AuthorRepositoryTest {

    private static final String AUTHOR_WITHOUT_BOOK = "name3";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private Book getBookWithManyAuthors() {
        var books = bookRepository.findAll();
        assertThat(books).isNotNull();
        books = books.stream().filter(b -> b.getAuthors().size() > 1).collect(Collectors.toList());
        assertThat(books).isNotEmpty();
        return books.get(0);
    }

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
    void shouldReturnAllAuthorsOfBook() {
        final var bookWithManyAuthors = getBookWithManyAuthors();

        final var authors = authorRepository.findAllByBooks_id(bookWithManyAuthors.getId());

        assertThat(authors).isNotNull().hasSize(bookWithManyAuthors.getAuthors().size());
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    public void removalOfTheAuthorWithTheBookShouldThrowException() {
        final var bookWithManyAuthors = getBookWithManyAuthors();
        final var author = bookWithManyAuthors.getAuthors().get(0);

        assertThatCode(() -> authorRepository.delete(author)).isInstanceOf(RepositoryException.class);
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    public void removalOfTheAuthorWithOutTheBookShouldDeleteAuthor() {
        final var author = authorRepository.findByName(AUTHOR_WITHOUT_BOOK).orElseGet(() -> fail("author not found"));
        assertThat(author.getBooks()).isEmpty();

        assertThatCode(() -> authorRepository.delete(author)).doesNotThrowAnyException();

        assertThat(authorRepository.findByName(AUTHOR_WITHOUT_BOOK)).isEmpty();
    }
}
