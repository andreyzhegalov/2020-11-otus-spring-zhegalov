package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMostOnce;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dao.AuthorDao;
import ru.otus.spring.hw.dao.BookDao;
import ru.otus.spring.hw.dao.GenreDao;
import ru.otus.spring.hw.dao.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

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

    @MockBean
    private GenreDao genreDao;

    @Test
    void saveBookTest() {
        final var author = new Author(1L, "name");
        final var genre = new Genre(2L, "genre");
        given(authorDao.getById(author.getId())).willReturn(Optional.of(author));
        given(genreDao.getById(genre.getId())).willReturn(Optional.of(genre));

        bookService.saveBook(new Book(3L, "title", author, genre));

        then(authorDao).should().getById(author.getId());
        then(genreDao).should().getById(genre.getId());
        then(bookDao).should().insertOrUpdate(any());
    }

    @Test
    void shouldReturnEmptyIfBookNotExisted() {
        final long id = 1L;
        given(bookDao.getById(id)).willReturn(Optional.empty());

        assertThat(bookService.getBook(id)).isEmpty();

        then(authorDao).shouldHaveNoInteractions();
        then(genreDao).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowExceptionIfAuthorNotExisted() {
        final long id = 1L;
        final long authorId = 2L;
        final long genreId = 1L;
        given(bookDao.getById(id)).willReturn(Optional.of(new BookDto(id, "title", authorId, genreId)));
        given(authorDao.getById(authorId)).willReturn(Optional.empty());

        assertThatCode(() -> bookService.getBook(id)).isInstanceOf(ServiceException.class);

        then(bookDao).should().getById(id);
        then(authorDao).should().getById(authorId);
        then(genreDao).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowExceptionIfGenreNotExisted() {
        final long id = 1L;
        final long authorId = 2L;
        final long genreId = 1L;
        given(bookDao.getById(id)).willReturn(Optional.of(new BookDto(id, "title", authorId, genreId)));
        given(authorDao.getById(authorId)).willReturn(Optional.of(new Author(authorId, "name")));
        given(genreDao.getById(genreId)).willReturn(Optional.empty());

        assertThatCode(() -> bookService.getBook(id)).isInstanceOf(ServiceException.class);

        then(bookDao).should().getById(id);
        then(authorDao).should().getById(authorId);
        then(genreDao).should().getById(genreId);
    }

    @Test
    void shouldReturnBook() {
        final long id = 1L;
        final long authorId = 2L;
        final long genreId = 2L;
        given(bookDao.getById(id)).willReturn(Optional.of(new BookDto(id, "title", authorId, genreId)));
        given(authorDao.getById(authorId)).willReturn(Optional.of(new Author(authorId, "name")));
        given(genreDao.getById(genreId)).willReturn(Optional.of(new Genre(genreId, "genre")));

        final var book = bookService.getBook(id);
        assertThat(book).isPresent();

        then(bookDao).should().getById(id);
        then(authorDao).should().getById(authorId);
    }

    @Test
    void shouldReturnListOfBooks() {
        bookService.getAllBooks();

        then(bookDao).should().getAll();
        then(bookDao).should(atMostOnce()).getById(anyLong());
        then(authorDao).should(atMostOnce()).getById(anyLong());
    }

    @Test
    void shouldDeleteBook() {
        final var id = 1L;
        bookService.deleteBook(id);
        then(bookDao).should().deleteBook(id);
    }

    @Test
    void shouldUpdateBook() {
        final var author = new Author(1L, "name");
        final var genre = new Genre(2L, "genre");
        final var book = new Book(1L, "title", author, genre);
        given(authorDao.getById(author.getId())).willReturn(Optional.of(author));
        given(genreDao.getById(genre.getId())).willReturn(Optional.of(genre));

        bookService.updateBook(book);

        then(bookDao).should().updateBook(any());
    }
}
