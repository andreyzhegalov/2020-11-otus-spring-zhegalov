package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ComponentScan({ "ru.otus.spring.hw.repositories" })
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldReturnCorrectBookWithAuthorList() {
        final var books = bookRepository.findAll();
        assertThat(books).isNotNull();
        assertThat(books).isNotEmpty();
        final var book = books.get(0);

        assertThat(book.getAuthors()).isNotNull().allMatch(Objects::nonNull)
                .allMatch(b -> b.getId() != null && !b.getId().isEmpty());
    }

    @Test
    void shouldReturnBookDtoListInTwoRequest() {
    }

    @Test
    void shouldReturnBookByIdInTwoRequest() {
    }

    @Test
    void shouldInsertAuthorToBookAuthorsList() {
    }

    @Test
    void shouldRemoveAuthorFromBookAuthors() {
    }
}
