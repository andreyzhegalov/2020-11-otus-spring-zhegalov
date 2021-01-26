package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import ru.otus.spring.hw.event.BookMongoEventListener;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@Import(BookMongoEventListener.class)
public class BookRepositoryTest extends AbstractRepositoryTest {
    private static final String BOOK_WITH_COMMENTS = "book1";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldReturnCorrectBookWithAuthorList() {
        final var books = bookRepository.findAll();
        assertThat(books).isNotNull();
        assertThat(books).isNotEmpty();
        final var book = books.get(0);

        assertThat(book.getAuthors()).isNotNull().allMatch(Objects::nonNull)
                .allMatch(b -> b.getId() != null && !b.getId().isEmpty());
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void deletingBookShouldDeleteBookFromAuthors() {
        var books = bookRepository.findAll();
        assertThat(books).isNotNull();
        books = books.stream().filter(b -> b.getAuthors().size() > 1).collect(Collectors.toList());
        assertThat(books).isNotEmpty();
        final var bookWithManyAuthors = books.get(0);
        assertThat(authorRepository.findAllByBooks_id(bookWithManyAuthors.getId())).isNotEmpty();

        bookRepository.delete(bookWithManyAuthors);

        assertThat(authorRepository.findAllByBooks_id(bookWithManyAuthors.getId())).isEmpty();
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void deletingBookShouldDeleteAllBookComments() {
        final var bookWithComments = bookRepository.findByTitle(BOOK_WITH_COMMENTS)
                .orElseGet(() -> fail("book not found"));
        assertThat(commentRepository.findAllByBook_id(bookWithComments.getId())).isNotEmpty();

        bookRepository.delete(bookWithComments);

        assertThat(commentRepository.findAllByBook_id(bookWithComments.getId())).isEmpty();
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void savedNewBookShouldThrowExceptionIfAuthorNotExist() {
        final var notExistedAuthor = new Author("not existed id", "unknown author", Collections.emptyList());
        assertThat(authorRepository.findById(notExistedAuthor.getId())).isEmpty();
        final var existedGenre = genreRepository.findByName("genre3").orElseGet(() -> fail("genre not exist"));

        final var newBook = new Book("new book", existedGenre, notExistedAuthor);

        assertThatCode(() -> bookRepository.save(newBook)).isInstanceOf(RepositoryException.class);
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void aNewBookShouldBeAddedToTheExistedAuthor() {
        final var existedAuthor = authorRepository.findByName("name3").orElseGet(() -> fail("author not exist"));
        final var existedGenre = genreRepository.findByName("genre3").orElseGet(() -> fail("genre not exist"));

        final var newTitle = "new book";
        assertThat(bookRepository.findByTitle(newTitle)).isEmpty();
        final var newBook = new Book(newTitle, existedGenre, existedAuthor);

        final var savedBook = bookRepository.save(newBook);

        final var updatedAuthor = authorRepository.findById(existedAuthor.getId())
                .orElseGet(() -> fail("updated author not exist"));

        assertThat(updatedAuthor.getBooks()).containsOnlyOnce(savedBook);
    }
}
