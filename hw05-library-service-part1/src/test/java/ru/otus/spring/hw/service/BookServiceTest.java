package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dao.BookDao;
import ru.otus.spring.hw.model.Book;

@SpringBootTest
public class BookServiceTest {

    @Import(BookServiceImpl.class)
    @Configuration
    public static class BookServiceTestInner {
    }

    @Autowired
    private BookServiceImpl bookService;

    @MockBean
    private BookDao bookDao;

    @Test
    void saveBookTest() {
        bookService.saveBook(new Book());
        // then(bookDao).should().insertOrUpdate(any());
    }
}
