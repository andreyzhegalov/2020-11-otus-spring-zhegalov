package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.model.Genre;

@Import(BookRepositoryJpa.class)
@DataJpaTest
public class BookRepositoryJpaTest {
    private final static int BOOK_COUNT = 2;
    private final static long EXISTED_BOOK_ID = 1L;
    private final static long NOT_EXISTED_BOOK_ID = 3L;

    @Autowired
    private BookRepositoryJpa bookRepository;

    @Autowired
    private TestEntityManager em;

    private Statistics statistic;

    @BeforeEach
    void setUp() {
        final SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        statistic = sessionFactory.getStatistics();
        statistic.setStatisticsEnabled(true);
    }

    @AfterEach
    void tearDown() {
        statistic.clear();
    }

    @Test
    void shouldReturnBookList() {
        var books = bookRepository.findAll();
        assertThat(books).isNotNull().hasSize(BOOK_COUNT).allMatch(b -> b != null)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getGenre() != null && b.getGenre().getName() != "")
                .allMatch(b -> b.getAuthor() != null && b.getAuthor().getName() != "")
                .allMatch(b -> b.getComments() != null && b.getComments().size() > 0);
        assertThat(statistic.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void shouldReturnBookByIdWhenBookExisted() {
        final var book = bookRepository.findById(EXISTED_BOOK_ID);
        assertThat(book).isPresent().get().extracting("id").isEqualTo(EXISTED_BOOK_ID);
        assertThat(book.get().getAuthor()).isNotNull().extracting("name").isNotEqualTo("");
        assertThat(book.get().getGenre()).isNotNull().extracting("name").isNotEqualTo("");
        assertThat(book.get().getComments()).isNotNull().isNotEmpty();
        assertThat(statistic.getPrepareStatementCount()).isEqualTo(2);
    }

    @Test
    void shouldNotReturnBookByIdForNotExistingBook() {
        assertThat(bookRepository.findById(NOT_EXISTED_BOOK_ID)).isNotPresent();
    }

    @Test
    void shouldUpdateBookIfIdExist() {
        final var initBook = bookRepository.findById(EXISTED_BOOK_ID).orElseGet(() -> fail("item not exist"));
        final var updatedAuthor = new Author(initBook.getAuthor().getId(), initBook.getAuthor().getName() + "_modify");
        final var updatedGenre = new Genre(initBook.getGenre().getId(), initBook.getGenre().getName() + "_modify");
        final var updatedBook = new Book(initBook.getId(), initBook.getTitle() + "_modify", updatedAuthor,
                updatedGenre);

        bookRepository.save(updatedBook);
        em.flush();
        em.clear();

        assertThat(bookRepository.findById(EXISTED_BOOK_ID)).isPresent().get().usingRecursiveComparison()
                .isEqualTo(updatedBook);
        assertThat(bookRepository.findAll()).hasSize(BOOK_COUNT);

        assertThat(statistic.getEntityUpdateCount()).isEqualTo(3);
    }

    @Test
    void shouldInsertIfBookIdNotExisted() {
        final var notExistedAuthor = new Author("new author");
        final var notExistedGenre = new Genre("new genre");
        final var notExistedBook = new Book("new title", notExistedAuthor, notExistedGenre);
        notExistedBook.addComment(new Comment("new comment 1"));
        notExistedBook.addComment(new Comment("new comment 2"));

        final var insertedBook = bookRepository.save(notExistedBook);
        final var insertedBookId = insertedBook.getId();
        em.flush();
        em.clear();

        assertThat(bookRepository.findById(insertedBookId)).isPresent().get().usingRecursiveComparison()
                .isEqualTo(notExistedBook);
        assertThat(bookRepository.findAll()).hasSize(BOOK_COUNT + 1);

        assertThat(statistic.getEntityUpdateCount()).isZero();
        assertThat(statistic.getEntityInsertCount()).isEqualTo(5);
    }

    @Test
    void addCommentShouldInsertNewItemToDb() {
        final var initBook = bookRepository.findById(EXISTED_BOOK_ID).orElseGet(() -> fail("item not exist"));
        final var initCommentCount = initBook.getComments().size();
        initBook.addComment(new Comment("new comment"));
        em.clear();

        bookRepository.save(initBook);
        em.flush();
        em.clear();

        assertThat(bookRepository.findById(EXISTED_BOOK_ID)).isPresent().get().extracting("comments")
                .matches(c -> c != null);
        assertThat(bookRepository.findById(EXISTED_BOOK_ID).get().getComments()).hasSize(initCommentCount + 1);

        assertThat(statistic.getEntityUpdateCount()).isZero();
        assertThat(statistic.getEntityInsertCount()).isEqualTo(1);
    }

    @Test
    void removeBookShouldDeleteComments() {
        final var initBook = bookRepository.findById(EXISTED_BOOK_ID).orElseGet(() -> fail("item not exist"));
        assertThat(initBook.getComments()).isNotEmpty();
        em.clear();

        bookRepository.remove(initBook.getId());
        em.flush();
        em.clear();

        assertThat(statistic.getEntityDeleteCount()).isEqualTo(2);
    }

    @Test
    void deletingANonExistingBookShouldThrowAnException() {
        assertThatCode(() -> bookRepository.remove(NOT_EXISTED_BOOK_ID)).isInstanceOf(RepositoryException.class);
        assertThat(bookRepository.findAll()).hasSize(BOOK_COUNT);
    }

}
