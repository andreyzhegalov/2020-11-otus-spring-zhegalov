package ru.otus.spring.hw.shell;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;

@SpringBootTest
class ApplicationCommandsTest {

    @Autowired
    private Shell shell;


    @Test
    void printShouldPrintAllBooks() {
        shell.evaluate(() -> "pb");
        // then(bookService).should().findAll();
        // then(ioBookService).should().print(any());
    }
}
