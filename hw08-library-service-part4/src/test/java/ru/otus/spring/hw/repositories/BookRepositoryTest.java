package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import ru.otus.spring.hw.event.BookMongoEventListener;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.model.Genre;

@Import(BookMongoEventListener.class)
public class BookRepositoryTest extends AbstractRepositoryTest {
    private static final String BOOK_WITH_COMMENTS = "book1";
    private static final String EXISTED_AUTHOR_NAME = "name3";
    private static final String EXISTED_GENRE_NAME = "genre3";

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private BookRepository bookRepository;

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void deletingBookShouldDeleteBookFromAuthors() {
        final Function<String, List<Author>> getAuthorsWithBook = bookId -> mongoOperations
                .find(query(where("books.id").is(bookId)), Author.class);

        var books = bookRepository.findAll();
        assertThat(books).isNotNull();
        books = books.stream().filter(b -> b.getAuthors().size() > 1).collect(Collectors.toList());
        assertThat(books).isNotEmpty();
        final var bookWithManyAuthors = books.get(0);

        assertThat(getAuthorsWithBook.apply(bookWithManyAuthors.getId())).isNotEmpty();

        bookRepository.delete(bookWithManyAuthors);

        assertThat(getAuthorsWithBook.apply(bookWithManyAuthors.getId())).isEmpty();
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void deletingBookShouldDeleteAllBookComments() {
        final Function<String, List<Comment>> commentsByBookId = bookId -> mongoOperations
                .find(query(where("book.id").is(bookId)), Comment.class);

        final var bookWithComments = bookRepository.findByTitle(BOOK_WITH_COMMENTS)
                .orElseGet(() -> fail("book not found"));

        assertThat(commentsByBookId.apply(bookWithComments.getId())).isNotEmpty();

        bookRepository.delete(bookWithComments);

        assertThat(commentsByBookId.apply(bookWithComments.getId())).isEmpty();
    }

    private Genre getExistedGenre() {
        return Optional.ofNullable(mongoOperations.findOne(query(where("name").is(EXISTED_GENRE_NAME)), Genre.class))
                .orElseGet(() -> fail("genre not exist"));
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void savedNewBookShouldThrowExceptionIfAuthorNotExist() {
        final var notExistedAuthor = new Author("not existed id", "unknown author", Collections.emptyList());
        final var authorWithId = mongoOperations.findById(notExistedAuthor.getId(), Author.class);
        assertThat(authorWithId).isNull();

        final var newBook = new Book("new book", getExistedGenre(), notExistedAuthor);

        assertThatCode(() -> bookRepository.save(newBook)).isInstanceOf(RepositoryException.class);
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void aNewBookShouldBeAddedToTheExistedAuthor() {
        final var existedAuthor = Optional
                .ofNullable(mongoOperations.findOne(query(where("name").is(EXISTED_AUTHOR_NAME)), Author.class))
                .orElseGet(() -> fail("author not exist"));
        final var newTitle = "new book";

        assertThat(bookRepository.findByTitle(newTitle)).isEmpty();
        final var newBook = new Book(newTitle, getExistedGenre(), existedAuthor);

        final var savedBook = bookRepository.save(newBook);

        final var updatedAuthor = Optional.ofNullable(mongoOperations.findById(existedAuthor.getId(), Author.class))
                .orElseGet(() -> fail("updated author not exist"));

        assertThat(updatedAuthor.getBooks()).containsOnlyOnce(savedBook);
    }
}
