

    package ru.otus.spring.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BookRepositoryTest {
    private final static int BOOK_COUNT = 2;
    private final static long EXISTED_BOOK_ID = 1L;

    @Autowired
    private BookRepository bookRepository;


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
