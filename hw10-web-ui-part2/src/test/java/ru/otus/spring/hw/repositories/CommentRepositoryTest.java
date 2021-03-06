package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ru.otus.spring.hw.controllers.dto.CommentDto;

public class CommentRepositoryTest extends AbstractRepositoryTest {
    private static final String BOOK_WITH_COMMENTS = "book1";
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldReturnCorrectCommentList() {
        final var comments = commentRepository.findAll();
        assertThat(comments).hasSize(1);
    }

    @Test
    void shouldReturnAllBookComments() {
        final var bookWithComments = bookRepository.findByTitle(BOOK_WITH_COMMENTS)
                .orElseGet(() -> fail("book not found"));
        final var bookComments = commentRepository.findAllByBook_id(bookWithComments.getId());
        assertThat(bookComments).isNotEmpty().allMatch(Objects::nonNull);
    }

    @Test
    void shouldReturnAllCommentDto() {
        final var commentDtoList = commentRepository.findAllDto();
        assertThat(commentDtoList).isNotEmpty().doesNotContainNull().allMatch(c -> c.getClass() == CommentDto.class)
                .allMatch(c -> Objects.nonNull(c.getBookId())).allMatch(c -> Objects.nonNull(c.getId()));

        final var commentDto = commentDtoList.get(0);
        assertThat(bookRepository.findById(commentDto.getBookId())).isNotEmpty();
    }

    @Test
    void shouldReturnCommentDtoByBookId() {
        final var bookWithComments = bookRepository.findByTitle(BOOK_WITH_COMMENTS)
                .orElseGet(() -> fail("book not found"));
        assertThat(commentRepository.findAllByBook_id(bookWithComments.getId())).isNotEmpty();

        final var commentDtoList = commentRepository.findAllDtoByBookId(bookWithComments.getId());
        assertThat(commentDtoList).isNotEmpty().doesNotContainNull().allMatch(c -> c.getClass() == CommentDto.class)
                .allMatch(c -> Objects.nonNull(c.getBookId())).allMatch(c -> Objects.nonNull(c.getId()));

        final var commentDto = commentDtoList.get(0);
        assertThat(bookRepository.findById(commentDto.getBookId())).isNotEmpty();
    }

    @Test
    void shouldReturnEmptyListCommentsForNotExistedBook() {
        assertThat(commentRepository.findAllDtoByBookId("not_existed_book_id")).isEmpty();
    }
}
