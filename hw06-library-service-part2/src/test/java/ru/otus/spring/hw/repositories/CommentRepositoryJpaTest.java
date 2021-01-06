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

@Import(CommentRepositoryJpa.class)
@DataJpaTest
public class CommentRepositoryJpaTest {

    private static final int COMMENT_COUNT = 2;

    private static final long EXISTED_COMMENT_ID = 1L;

    private static final long NOT_EXISTED_COMMENT_ID = 5L;

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
    void shouldReturnCommentList() {
        var comments = commentRepository.findAll();
        assertThat(comments).isNotNull().hasSize(COMMENT_COUNT).allMatch(c -> c != null && !c.getText().equals(""))
                .allMatch(c -> c.getBook() != null && c.getBook().hasId())
                .allMatch(c -> c.getBook().getGenre() != null);

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

        assertThat(statistic.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void shouldNotReturnCommentByIdForNotExistingComment() {
        assertThat(commentRepository.findById(NOT_EXISTED_COMMENT_ID)).isNotPresent();
        assertThat(statistic.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void shouldThrowExceptionIfTheBookNotExist() {
        final var updatedComment = new Comment("name", new Book());
        assertThat(updatedComment.hasId()).isFalse();

        assertThatCode(() -> commentRepository.save(updatedComment)).isInstanceOf(RepositoryException.class);

        assertThat(statistic.getPrepareStatementCount()).isZero();
        assertThat(statistic.getEntityInsertCount()).isZero();
        assertThat(statistic.getEntityUpdateCount()).isZero();
        assertThat(statistic.getEntityDeleteCount()).isZero();
    }

    @Test
    void shouldInsertIfCommentIdNotExisted() {
        final var newComment = new Comment("name",
                new Book(1L, "title", new Author(1L, "name"), new Genre(1L, "genre")));
        assertThat(newComment.hasId()).isFalse();

        final var comment = commentRepository.save(newComment);
        em.flush();
        em.clear();

        assertThat(comment.hasId()).isTrue();
        assertThat(commentRepository.findAll()).hasSize(COMMENT_COUNT + 1);

        assertThat(statistic.getEntityInsertCount()).isEqualTo(1);
        assertThat(statistic.getEntityUpdateCount()).isZero();
        assertThat(statistic.getEntityDeleteCount()).isZero();
    }

    @Test
    void shouldUpdateCommentIfIdExist() {
        final var comment = commentRepository.findById(EXISTED_COMMENT_ID).orElseGet(() -> fail("comment not exist"));
        comment.setText(comment.getText() + "_modify");

        assertThatCode(() -> commentRepository.save(comment)).doesNotThrowAnyException();
        em.flush();
        em.clear();

        assertThat(commentRepository.findById(EXISTED_COMMENT_ID)).isPresent().get().isEqualTo(comment);
        assertThat(commentRepository.findAll()).hasSize(COMMENT_COUNT);

        assertThat(statistic.getEntityInsertCount()).isZero();
        assertThat(statistic.getEntityUpdateCount()).isEqualTo(1);
        assertThat(statistic.getEntityDeleteCount()).isZero();
    }

    @Test
    void deletingAExistingCommentShouldDeleteComment() {
        commentRepository.findById(EXISTED_COMMENT_ID).orElseGet(() -> fail("comment not exist"));
        em.clear();

        commentRepository.remove(EXISTED_COMMENT_ID);
        em.clear();
        em.flush();

        assertThat(commentRepository.findById(EXISTED_COMMENT_ID)).isNotPresent();
        assertThat(commentRepository.findAll()).hasSize(COMMENT_COUNT - 1);
    }

    @Test
    void deletingANonExistingCommentShouldThrowAnException() {
        assertThatCode(() -> commentRepository.remove(NOT_EXISTED_COMMENT_ID)).isInstanceOf(RepositoryException.class);
        assertThat(commentRepository.findAll()).hasSize(COMMENT_COUNT);

        assertThat(statistic.getEntityInsertCount()).isZero();
        assertThat(statistic.getEntityUpdateCount()).isZero();
        assertThat(statistic.getEntityDeleteCount()).isZero();
    }

    @Test
    void shouldDeleteAllCommentsByBookId() {
        final var existedBookId = 1L;
        final var commentsCount = commentRepository.findAll().size();
        em.clear();

        commentRepository.deleteByBookId(existedBookId);
        em.flush();
        em.clear();

        assertThat(commentRepository.findAll()).hasSizeLessThan(commentsCount);
    }

}
