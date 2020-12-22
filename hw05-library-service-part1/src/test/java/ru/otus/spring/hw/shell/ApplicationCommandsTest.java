package ru.otus.spring.hw.shell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;

import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.IOBookService;

@SpringBootTest
class ApplicationCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private BookService bookService;

    @MockBean
    private IOBookService ioBookService;

    @Test
    void printShouldPrintAllBooks() {
        shell.evaluate(() -> "p");
        then(bookService).should().getAllBooks();
        then(ioBookService).should().printBooks(any());
    }

    @Test
    void shouldReadNewBook() {
        shell.evaluate(() -> "a");
        then(ioBookService).should().getBook();
        then(bookService).should().saveBook(any());
    }
}
