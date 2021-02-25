package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.time.Duration;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
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
    private static final Duration TIMEOUT = Duration.ofMillis(100);

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    @Autowired
    private BookRepository bookRepository;

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void deletingBookShouldDeleteBookFromAuthors() {
        final Function<String, Flux<Author>> getAuthorsWithBook = bookId -> mongoOperations
                .find(query(where("books.id").is(bookId)), Author.class);
        final var bookWithManyAuthors = bookRepository.findAll().filter(b -> b.getAuthors().size() > 1)
                .blockFirst(TIMEOUT);
        assertThat(bookWithManyAuthors).isNotNull();
        assertThat(getAuthorsWithBook.apply(bookWithManyAuthors.getId()).buffer().blockFirst()).isNotEmpty();

        StepVerifier.create(bookRepository.delete(bookWithManyAuthors)).expectComplete().verify(TIMEOUT);

        assertThat(getAuthorsWithBook.apply(bookWithManyAuthors.getId()).blockFirst()).isNull();
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void deletingBookShouldDeleteAllBookComments() {
        final Function<String, Flux<Comment>> getCommentsByBookId = bookId -> mongoOperations
                .find(query(where("book.id").is(bookId)), Comment.class);
        final var bookWithComments = bookRepository.findByTitle(BOOK_WITH_COMMENTS).blockOptional(TIMEOUT)
                .orElseGet(() -> fail("book not found"));
        assertThat(getCommentsByBookId.apply(bookWithComments.getId()).blockFirst()).isNotNull();

        StepVerifier.create(bookRepository.delete(bookWithComments)).expectComplete().verify(TIMEOUT);

        assertThat(getCommentsByBookId.apply(bookWithComments.getId()).blockFirst()).isNull();
    }

    private Genre getExistedGenre() {
        return mongoOperations.findOne(query(where("name").is(EXISTED_GENRE_NAME)), Genre.class).blockOptional(TIMEOUT)
                .orElseGet(() -> fail("author not exist"));
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void aNewBookShouldBeAddedToTheExistedAuthor() throws InterruptedException {
        final var existedAuthor = mongoOperations.findOne(query(where("name").is(EXISTED_AUTHOR_NAME)), Author.class)
                .blockOptional(TIMEOUT).orElseGet(() -> fail("author not exist"));
        final var newTitle = "new book";
        assertThat(bookRepository.findByTitle(newTitle).blockOptional(TIMEOUT)).isEmpty();
        final var newBook = new Book(newTitle, getExistedGenre(), existedAuthor);

        final var savedBook = bookRepository.save(newBook).log().block(TIMEOUT);
        // needed to end onAfterSave in the MongoEventLoop
        Thread.sleep(100);

        var updatedAuthor = mongoOperations.findById(existedAuthor.getId(), Author.class).block(TIMEOUT);
        assertThat(updatedAuthor).isNotNull();
        assertThat(updatedAuthor.getBooks()).containsOnlyOnce(savedBook);
    }
}
