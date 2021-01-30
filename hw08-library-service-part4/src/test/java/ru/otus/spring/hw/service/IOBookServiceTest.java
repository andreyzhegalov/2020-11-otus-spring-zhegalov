package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

@SpringBootTest
public class IOBookServiceTest {
    @Import(IOBookService.class)
    @Configuration
    public static class IOBookServiceTestInner {
    }

    @Autowired
    private IOBookService ioBookService;

    @MockBean
    private IOService ioService;

    @Test
    void printBooksShouldPrintViaIOService() {
        ioBookService.print(List.of(new Book("book1", new Genre()), new Book("book2", new Genre())));
        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(never()).read();
    }

    @Test
    void shouldReadBookFromIOService() {
        given(ioService.read()).willReturn("title").willReturn("1").willReturn("1");

        ioBookService.getBook();

        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(atLeastOnce()).read();
    }
}
