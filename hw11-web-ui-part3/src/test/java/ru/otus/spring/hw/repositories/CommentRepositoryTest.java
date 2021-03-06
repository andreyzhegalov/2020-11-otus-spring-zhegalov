package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.test.StepVerifier;
import ru.otus.spring.hw.controllers.dto.CommentDto;

public class CommentRepositoryTest extends AbstractRepositoryTest {
    private static final String BOOK_WITH_COMMENTS = "book1";
    private static final Duration TIMEOUT = Duration.ofMillis(1000);
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldReturnAllCommentDto() {
        StepVerifier.create(commentRepository.findAllDto())
                .assertNext(c -> assertThat(c).isNotNull().isInstanceOf(CommentDto.class)).verifyComplete();
    }

    @Test
    void allCommentDtoShouldHaveCorrectIdToTheBook() {
        final var commentDto = commentRepository.findAllDto().blockFirst(TIMEOUT);
        assertThat(commentDto).isNotNull();
        final var book = bookRepository.findById(commentDto.getBookId()).block(TIMEOUT);
        assertThat(book).isNotNull();
    }

    @Test
    void shouldReturnCommentDtoByBookId() {
        final var bookWithComments = bookRepository.findByTitle(BOOK_WITH_COMMENTS).blockOptional(TIMEOUT)
                .orElseGet(() -> fail("book not found"));
        assertThat(commentRepository.findAllByBook_id(bookWithComments.getId()).collectList().block(TIMEOUT))
                .isNotEmpty();
        StepVerifier.create(commentRepository.findAllDtoByBookId(bookWithComments.getId()).collectList())
                .assertNext(commentDtoList -> assertThat(commentDtoList).isNotEmpty().doesNotContainNull()
                        .allMatch(c -> c.getClass() == CommentDto.class).allMatch(c -> Objects.nonNull(c.getBookId()))
                        .allMatch(c -> Objects.nonNull(c.getId())))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyListCommentsForNotExistedBook() {
        StepVerifier.create(commentRepository.findAllDtoByBookId("not_existed_book_id")).expectComplete()
                .verify(TIMEOUT);
    }
}
