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
        assertThat(books).isNotNull().hasSize(BOOK_COUNT).allMatch(s -> !s.getTitle().equals(""))
                .allMatch(b -> b.getGenre() != null).allMatch(b -> b.getGenre().getName() != "")
                .allMatch(b -> b.getAuthor() != null).allMatch(b -> b.getAuthor().getName() != "")
                .allMatch(b -> b.getComments() != null && b.getComments().size() > 0);
        assertThat(statistic.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void shouldReturnBookByIdWhenBookExisted() {
        final var book = bookRepository.findById(EXISTED_BOOK_ID);
        assertThat(book).isPresent().get().extracting("id").isEqualTo(EXISTED_BOOK_ID);
        assertThat(book.get().getAuthor()).isNotNull().extracting("name").isNotEqualTo("");
        assertThat(book.get().getGenre()).isNotNull().extracting("name").isNotEqualTo("");
        assertThat(statistic.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void shouldNotReturnBookByIdForNotExistingBook() {
        assertThat(bookRepository.findById(NOT_EXISTED_BOOK_ID)).isNotPresent();
    }

    @Test
    void shouldUpdateBookIfIdExist() {
        final var initBook = bookRepository.findById(EXISTED_BOOK_ID).orElseGet(() -> fail("genre not exist"));
        final var updatedAuthor = new Author(initBook.getAuthor().getId(), initBook.getAuthor().getName() + "_modify");
        final var updatedGenre = new Genre(initBook.getGenre().getId(), initBook.getGenre().getName() + "_modify");
        final var updatedBook = new Book(initBook.getId(), initBook.getTitle() + "_modify", updatedAuthor,
                updatedGenre);

        bookRepository.save(updatedBook);
        em.flush();

        assertThat(bookRepository.findById(EXISTED_BOOK_ID)).isPresent().get().usingRecursiveComparison()
                .isEqualTo(updatedBook);
        assertThat(bookRepository.findAll().size()).isEqualTo(BOOK_COUNT);
        assertThat(statistic.getEntityUpdateCount()).isEqualTo(3);
    }

    @Test
    void shouldInsertIfBookIdNotExisted() {
        final var notExistedAuthor = new Author(0L, "new author");
        final var notExistedGenre = new Genre(0L, "new genre");
        final var notExistedBook = new Book(0L, "new title", notExistedAuthor, notExistedGenre);
        notExistedBook.addComment(new Comment(0L, "new comment 1"));
        notExistedBook.addComment(new Comment(0L, "new comment 2"));

        final var insertedBook = bookRepository.save(notExistedBook);
        em.flush();

        final var insertedBookId = insertedBook.getId();
        final var mayBeBook = bookRepository.findById(insertedBookId);
        assertThat(mayBeBook).isPresent().get().extracting("title").isEqualTo(notExistedBook.getTitle());
        assertThat(mayBeBook.get().getAuthor()).isNotNull().extracting("name").isEqualTo(notExistedAuthor.getName());
        assertThat(mayBeBook.get().getGenre()).isNotNull().extracting("name").isEqualTo(notExistedGenre.getName());
        assertThat(mayBeBook.get().getComments()).isNotNull().hasSize(2);
        assertThat(bookRepository.findAll().size()).isEqualTo(BOOK_COUNT + 1);

        assertThat(statistic.getEntityUpdateCount()).isEqualTo(0);  // Not worked. How to control update queries?
        assertThat(statistic.getEntityInsertCount()).isEqualTo(5);
    }

    @Test
    void deletingAExistingBookShouldDeleteBook() {
        final var mayBeBook = bookRepository.findById(EXISTED_BOOK_ID);
        assertThat(mayBeBook).isPresent().get().isInstanceOf(Book.class);
        em.detach(mayBeBook.get());

        bookRepository.remove(EXISTED_BOOK_ID);

        assertThat(bookRepository.findById(EXISTED_BOOK_ID)).isNotPresent();
        assertThat(bookRepository.findAll()).hasSize(BOOK_COUNT - 1);
    }

    @Test
    void deletingANonExistingBookShouldThrowAnException() {
        assertThatCode(() -> bookRepository.remove(NOT_EXISTED_BOOK_ID)).isInstanceOf(RepositoryException.class);
        assertThat(bookRepository.findAll()).hasSize(BOOK_COUNT);
    }

}
