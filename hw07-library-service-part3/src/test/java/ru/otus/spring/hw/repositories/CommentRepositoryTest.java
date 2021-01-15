package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;

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

import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.model.Book;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentRepositoryTest {

    private static final int COMMENT_COUNT = 2;

    private static final long EXISTED_COMMENT_ID = 1L;

    @Autowired
    private CommentRepository commentRepository;

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
    void shouldReturnCommentDtoForAllComments() {
        var commentsDto = commentRepository.findAllBy(CommentDto.class);
        assertThat(commentsDto).isNotNull().hasSize(COMMENT_COUNT).allMatch(cd -> cd.getClass() == CommentDto.class);

        assertThat(statistic.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void shouldReturnCommentByIdWhenCommentExisted() {
        final var comment = commentRepository.findById(EXISTED_COMMENT_ID);

        assertThat(comment).isPresent();
        assertThat(comment.get().getId()).isEqualTo(EXISTED_COMMENT_ID);
        assertThat(comment.get().getText()).isNotNull();
        assertThat(comment.get().getBook().getId()).isNotNull();
        assertThat(comment.get().getBook().getGenre()).isNotNull();
        assertThat(comment.get().getBook().getAuthors()).isNotEmpty();

        assertThat(statistic.getPrepareStatementCount()).isEqualTo(3); // + select book + 1 sub query for author in the
                                                                       // book
    }

    @Test
    void shouldDeleteCommentAfterBookWasDeleted() {
        final var existedBookId = 1L;
        final var book = em.find(Book.class, existedBookId);
        assertThat(book).isNotNull();

        assertThat(commentRepository.findAll().stream().filter(c -> c.getBook().getId() == existedBookId)
                .collect(Collectors.toList())).isNotEmpty();

        em.remove(book);
        em.flush();
        em.clear();

        assertThat(commentRepository.findAll().stream().filter(c -> c.getBook().getId() == existedBookId)
                .collect(Collectors.toList())).isEmpty();
    }
}
