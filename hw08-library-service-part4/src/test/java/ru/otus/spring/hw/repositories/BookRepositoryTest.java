package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import ru.otus.spring.hw.event.BookMongoEventListener;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ComponentScan({ "ru.otus.spring.hw.repositories" })
@Import(BookMongoEventListener.class)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldReturnCorrectBookWithAuthorList() {
        final var books = bookRepository.findAll();
        assertThat(books).isNotNull();
        assertThat(books).isNotEmpty();
        final var book = books.get(0);

        assertThat(book.getAuthors()).isNotNull().allMatch(Objects::nonNull)
                .allMatch(b -> b.getId() != null && !b.getId().isEmpty());
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void deletingBookShouldDeleteBookFromAuthors() {
        var books = bookRepository.findAll();
        assertThat(books).isNotNull();
        books = books.stream().filter(b -> b.getAuthors().size() > 1).collect(Collectors.toList());
        assertThat(books).isNotEmpty();
        final var bookWithManyAuthors = books.get(0);
        assertThat(authorRepository.findAllByBooks_id(bookWithManyAuthors.getId())).isNotEmpty();

        bookRepository.delete(bookWithManyAuthors);

        assertThat(authorRepository.findAllByBooks_id(bookWithManyAuthors.getId())).isEmpty();
    }
}
