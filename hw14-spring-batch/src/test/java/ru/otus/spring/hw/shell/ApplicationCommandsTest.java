package ru.otus.spring.hw.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@SpringBootTest
class ApplicationCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void printShouldPrintAllBooks() {
        shell.evaluate(() -> "pb");
        // then(bookService).should().findAll();
        // then(ioBookService).should().print(any());
    }
}
