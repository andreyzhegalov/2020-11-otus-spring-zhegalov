package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ru.otus.spring.hw.model.Book;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BookRepositoryTest {
    private final static int BOOK_COUNT = 2;
    private final static long EXISTED_BOOK_ID = 1L;

    @Autowired
    private BookRepository bookRepository;

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
    void shouldReturnBookDtoListInTwoRequest() {
        var booksDto = bookRepository.findAll();

        assertThat(booksDto).isNotNull().hasSize(BOOK_COUNT).allMatch(Objects::nonNull)
                .allMatch(bd -> bd.getClass() == Book.class).allMatch(b -> !b.getTitle().equals(""))
                .allMatch(bd -> bd.getAuthors().size() > 0);
        assertThat(statistic.getPrepareStatementCount()).isEqualTo(2); // + authors sub query
    }

    @Test
    void shouldReturnBookByIdInTwoRequest() {
        final var authorsCount = 2;

        final var book = bookRepository.findById(EXISTED_BOOK_ID);

        assertThat(book).isPresent().get().extracting("id").isEqualTo(EXISTED_BOOK_ID);
        assertThat(book.get().getAuthors()).isNotNull().hasSize(authorsCount)
                .allMatch(a -> a != null && !a.getName().equals(""));
        assertThat(book.get().getGenre()).isNotNull().extracting("name").isNotEqualTo("");

        assertThat(statistic.getPrepareStatementCount()).isEqualTo(2); // + authors sub query
    }
}
