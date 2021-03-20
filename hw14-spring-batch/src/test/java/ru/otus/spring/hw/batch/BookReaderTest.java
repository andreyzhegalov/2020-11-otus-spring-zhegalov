package ru.otus.spring.hw.batch;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

@SpringBootTest
public class BookReaderTest {

    @Autowired
    private BookReader bookReader;

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnAllBooks() throws Exception {
        assertThat(bookReader).isNotNull();
        assertThat(bookReader.read()).isInstanceOf(Book.class);
        assertThat(bookReader.read()).isInstanceOf(Book.class);
        assertThat(bookReader.read()).isNull();
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void readItemShouldReturnBookWithGenreAndAuthors() throws Exception {
        final var book = bookReader.read();
        assertThat(book).isNotNull();
        assertThat(book.getId()).isNotNull();
        assertThat(book.getTitle()).isNotNull().isNotBlank();
        assertThat(book.getGenre()).isNotNull().isInstanceOf(Genre.class).extracting("name").isNotNull();
        assertThat(book.getAuthors()).isNotNull().isNotEmpty();
        final var authorList = book.getAuthors();
        authorList.forEach((a -> {
            assertThat(a).isNotNull().isInstanceOf(Author.class);
            assertThat(a.getId()).isNotNull();
            assertThat(a.getName()).isNotBlank();
        }));
    }

}
