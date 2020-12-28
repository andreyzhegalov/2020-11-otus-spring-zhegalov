package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.model.Comment;

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

    @Test
    void shouldReturnCommentList() {
        var comments = commentRepository.findAll();
        assertThat(comments).isNotNull().hasSize(COMMENT_COUNT).allMatch(s -> !s.getText().equals(""));
    }

    @Test
    void shouldReturnCommentByIdWhenCommentExisted() {
        assertThat(commentRepository.findById(EXISTED_COMMENT_ID)).isPresent().get().extracting("id")
                .isEqualTo(EXISTED_COMMENT_ID);
    }

    @Test
    void shouldNotReturnCommentByIdForNotExistingComment() {
        assertThat(commentRepository.findById(NOT_EXISTED_COMMENT_ID)).isNotPresent();
    }

    @Test
    void shouldUpdateCommentIfIdExist() {
        final var initComment = commentRepository.findById(EXISTED_COMMENT_ID)
                .orElseGet(() -> fail("comment not exist"));
        final var updatedComment = new Comment(initComment.getId(), initComment.getText() + "_modify");

        commentRepository.save(updatedComment);

        assertThat(commentRepository.findById(EXISTED_COMMENT_ID)).isPresent().get().isEqualTo(updatedComment);
        assertThat(commentRepository.findAll()).hasSize(COMMENT_COUNT);
    }

    @Test
    void shouldInsertIfCommentIdNotExisted() {
        final var updatedComment = new Comment(0L, "name");
        assertThat(updatedComment.hasId()).isFalse();

        final var comment = commentRepository.save(updatedComment);

        assertThat(comment.hasId()).isTrue();
        assertThat(commentRepository.findAll()).hasSize(COMMENT_COUNT + 1);
    }

    @Test
    void deletingAExistingWorkbookShouldDeleteComment() {
        final var mayBeComment = commentRepository.findById(EXISTED_COMMENT_ID);
        assertThat(mayBeComment).isPresent();
        em.detach(mayBeComment.get());

        commentRepository.remove(EXISTED_COMMENT_ID);

        assertThat(commentRepository.findById(EXISTED_COMMENT_ID)).isNotPresent();
        assertThat(commentRepository.findAll()).hasSize(COMMENT_COUNT - 1);
    }

    @Test
    void deletingANonExistingWorkbookShouldThrowAnException() {
        assertThatCode(() -> commentRepository.remove(NOT_EXISTED_COMMENT_ID)).isInstanceOf(RepositoryException.class);
        assertThat(commentRepository.findAll()).hasSize(COMMENT_COUNT);
    }

}
