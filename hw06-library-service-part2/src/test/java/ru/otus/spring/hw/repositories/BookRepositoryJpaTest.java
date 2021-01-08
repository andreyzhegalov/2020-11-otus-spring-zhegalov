package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Objects;

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

        assertThat(books).isNotNull().hasSize(BOOK_COUNT).allMatch(Objects::nonNull)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getGenre() != null && !b.getGenre().getName().equals(""))
                .allMatch(b -> b.getAuthors() != null && b.getAuthors().size() > 0);
        assertThat(statistic.getPrepareStatementCount()).isEqualTo(2); // + authors sub query
    }

    @Test
    void shouldReturnBookByIdWhenBookExisted() {
        final var authorsCount = 2;

        final var book = bookRepository.findById(EXISTED_BOOK_ID);

        assertThat(book).isPresent().get().extracting("id").isEqualTo(EXISTED_BOOK_ID);
        assertThat(book.get().getAuthors()).isNotNull().hasSize(authorsCount)
                .allMatch(a -> a != null && !a.getName().equals(""));
        assertThat(book.get().getGenre()).isNotNull().extracting("name").isNotEqualTo("");

        assertThat(statistic.getPrepareStatementCount()).isEqualTo(2); // + authors sub query
    }

    @Test
    void shouldNotReturnBookByIdForNotExistingBook() {
        assertThat(bookRepository.findById(NOT_EXISTED_BOOK_ID)).isNotPresent();
    }

    @Test
    void shouldUpdateBookIfIdExist() {
        final var initBook = bookRepository.findById(EXISTED_BOOK_ID).orElseGet(() -> fail("item not exist"));
        final var updatedGenre = new Genre(initBook.getGenre().getId(), initBook.getGenre().getName() + "_modify");
        final var updatedBook = new Book(initBook.getId(), initBook.getTitle() + "_modify", updatedGenre);

        bookRepository.save(updatedBook);
        em.flush();
        em.clear();

        assertThat(bookRepository.findById(EXISTED_BOOK_ID)).isPresent();
        final var updatedBookFromDb = bookRepository.findById(EXISTED_BOOK_ID).orElseGet(() -> fail("item not exist"));
        assertThat(updatedBookFromDb.getId()).isEqualTo(updatedBook.getId());
        assertThat(updatedBookFromDb.getGenre()).isEqualTo(updatedBook.getGenre());
        assertThat(updatedBookFromDb.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(updatedBookFromDb.getAuthors()).hasSameElementsAs(updatedBook.getAuthors());

        assertThat(bookRepository.findAll()).hasSize(BOOK_COUNT);
        assertThat(statistic.getEntityUpdateCount()).isEqualTo(2);
    }

    @Test
    void shouldInsertIfBookIdNotExisted() {
        final var notExistedGenre = new Genre("new genre");
        final var notExistedBook = new Book("new title", notExistedGenre);

        final var insertedBook = bookRepository.save(notExistedBook);
        final var insertedBookId = insertedBook.getId();
        em.flush();
        em.clear();

        assertThat(bookRepository.findById(insertedBookId)).isPresent().get().usingRecursiveComparison()
                .isEqualTo(notExistedBook);
        assertThat(bookRepository.findAll()).hasSize(BOOK_COUNT + 1);

        assertThat(statistic.getEntityUpdateCount()).isZero();
        assertThat(statistic.getEntityInsertCount()).isEqualTo(2);
    }

    @Test
    void addExistedAuthorShouldInsertAuthorToBookAuthorsList() {
        final var newAuthorId = 3L;
        final var existedAuthor = em.find(Author.class, newAuthorId);
        assertThat(existedAuthor).isNotNull();
        final var initBook = bookRepository.findById(EXISTED_BOOK_ID).orElseGet(() -> fail("item not exist"));
        assertThat(initBook.getAuthors()).allMatch(a -> a.getId() != newAuthorId);
        final var initAuthorCount = initBook.getAuthors().size();

        initBook.addAuthor(existedAuthor);
        em.flush();
        em.clear();

        assertThat(bookRepository.findById(EXISTED_BOOK_ID)).isPresent();
        final var bookFromDb = bookRepository.findById(EXISTED_BOOK_ID).orElseGet(() -> fail("item not exist"));
        assertThat(bookFromDb.getAuthors()).hasSize(initAuthorCount + 1);

        assertThat(statistic.getEntityUpdateCount()).isZero();
        assertThat(statistic.getEntityInsertCount()).isZero();
        assertThat(statistic.getEntityDeleteCount()).isZero();
    }

    @Test
    void shouldRemoveAuthorFromBookAuthors() {
        final var new_author_id = 2L;
        final var existedAuthor = em.find(Author.class, new_author_id);
        assertThat(existedAuthor).isNotNull();
        final var initBook = bookRepository.findById(EXISTED_BOOK_ID).orElseGet(() -> fail("item not exist"));
        assertThat(initBook.getAuthors()).anyMatch(a -> a.getId() == new_author_id);
        final var initAuthorCount = initBook.getAuthors().size();

        initBook.removeAuthor(existedAuthor);
        em.flush();
        em.clear();

        assertThat(bookRepository.findById(EXISTED_BOOK_ID)).isPresent();
        final var bookFromDb = bookRepository.findById(EXISTED_BOOK_ID).orElseGet(() -> fail("item not exist"));
        assertThat(bookFromDb.getAuthors()).hasSize(initAuthorCount - 1);

        assertThat(statistic.getEntityUpdateCount()).isZero();
        assertThat(statistic.getEntityInsertCount()).isZero();
        assertThat(statistic.getEntityDeleteCount()).isZero();
    }

    @Test
    void shouldDeleteExistedBook() {
        bookRepository.remove(EXISTED_BOOK_ID);
        em.flush();
        em.clear();

        assertThat(bookRepository.findAll()).hasSize(BOOK_COUNT-1);
        assertThat(statistic.getEntityUpdateCount()).isZero();
        assertThat(statistic.getEntityInsertCount()).isZero();
        assertThat(statistic.getEntityDeleteCount()).isZero();
    }

    @Test
    void deletingANonExistingBookShouldThrowAnException() {
        assertThatCode(() -> bookRepository.remove(NOT_EXISTED_BOOK_ID)).isInstanceOf(RepositoryException.class);
        assertThat(bookRepository.findAll()).hasSize(BOOK_COUNT);
        assertThat(statistic.getEntityUpdateCount()).isZero();
        assertThat(statistic.getEntityInsertCount()).isZero();
        assertThat(statistic.getEntityDeleteCount()).isZero();
    }

}
