package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.anyString;
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

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@SpringBootTest
public class IOBookServiceImplTest {
    @Import(IOBookServiceImpl.class)
    @Configuration
    public static class IOBookServiceTestInner{
    }

    @Autowired
    private IOBookService ioBookService;

    @MockBean
    private IOService ioService;

    @Test
    void printBooksShouldPrintViaIOService(){
        final var book1 = new Book(1L, "title", new Author(1L,"name"));
        final var book2 = new Book(2L, "title", new Author(2L,"name"));

        ioBookService.printBooks(List.of( book1, book2));
        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(never()).read();
    }


    @Test
    void shouldReadBookFromIOService()
    {
        ioBookService.getBook();

        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(atLeastOnce()).read();
    }

}

