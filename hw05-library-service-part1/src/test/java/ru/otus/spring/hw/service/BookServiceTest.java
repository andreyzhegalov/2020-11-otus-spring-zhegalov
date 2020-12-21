package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dao.AuthorDao;
import ru.otus.spring.hw.dao.BookDao;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.dto.BookDto;

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

    @MockBean
    private AuthorDao authorDao;

    @Test
    void saveBookTest() {
        final var author = new Author("name");
        bookService.saveBook(new Book(1L, "title", author));
        then(authorDao).should().insertOrUpdate(eq(author));
        then(bookDao).should().insertOrUpdate(any());
    }

    @Test
    void shouldReturnEmptyIfBookNotExisted() {
        final long id = 1L;
        given(bookDao.getById(id)).willReturn(Optional.empty());

        assertThat(bookService.getBook(id)).isEmpty();

        then(authorDao).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowExceptionIfAuthorNotExisted() {
        final long id = 1L;
        final long authorId = 2L;
        given(bookDao.getById(id)).willReturn(Optional.of(new BookDto(id, "title", authorId)));
        given(authorDao.getById(authorId)).willReturn(Optional.empty());

        assertThatCode(() -> bookService.getBook(id)).isInstanceOf(ServiceException.class);

        then(bookDao).should().getById(id);
        then(authorDao).should().getById(authorId);
    }

    @Test
    void shouldReturnBook() {
        final long id = 1L;
        final long authorId = 2L;
        given(bookDao.getById(id)).willReturn(Optional.of(new BookDto(id, "title", authorId)));
        given(authorDao.getById(authorId)).willReturn(Optional.of(new Author(authorId, "name")));

        assertThat(bookService.getBook(id)).isPresent();

        then(bookDao).should().getById(id);
        then(authorDao).should().getById(authorId);
    }
}
