package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;

@DataMongoTest
@ComponentScan({ "ru.otus.spring.hw.repositories" })
public class BookRepositoryTest {
    private final static int BOOK_COUNT = 2;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldReturnCorrectBookList() {
        assertThat(bookRepository.findAll()).hasSize(BOOK_COUNT);
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
