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

import ru.otus.spring.hw.dto.BookDto;

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
        final var bookDto1 = new BookDto(1L, "title", 1L);
        final var bookDto2 = new BookDto(1L, "title", 1L);

        ioBookService.print(List.of(bookDto1, bookDto2));
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
