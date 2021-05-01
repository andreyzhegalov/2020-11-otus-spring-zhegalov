package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.mockito.internal.stubbing.answers.Returns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.controllers.dto.CommentDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;

@SpringBootTest
public class CommentServiceCircuitBreakerTest {
    private final static int CIRCUIT_BREAKER_TIMEOUT = 500;
    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void shouldReturnEmptyListIfRepositoryNotResponse() {
        final var commentList = new Returns(List.of(new Comment()));
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, commentList);
        doAnswer(answersWithDelay).when(commentRepository).findAllDto();

        final var commentListFromCircuitBreaker = commentService.findAll();

        then(commentRepository).should().findAllDto();
        assertThat(commentListFromCircuitBreaker).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenFindByIdIfRepositoryNotResponse() {
        final var commentList = new Returns(List.of(new Comment()));
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, commentList);
        doAnswer(answersWithDelay).when(commentRepository).findAllDtoByBookId("123");

        final var commentListFromCircuitBreaker = commentService.findAllByBookId("123");

        then(commentRepository).should().findAllDtoByBookId("123");
        assertThat(commentListFromCircuitBreaker).isEmpty();
    }

    @Test
    void shouldReturnEmptyCommentWhenSaveIfRepositryNotResponse() {
        final var bookId = "1";
        final var book = new Book();
        book.setId(bookId);
        final var commentDto = new CommentDto("new text", bookId);
        final var comment = new Comment();
        comment.setId("123");
        comment.setBook(book);

        final var commentReturn = new Returns(comment);
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, commentReturn);
        doAnswer(answersWithDelay).when(commentRepository).save(any());

        given(bookRepository.findById(bookId)).willReturn(Optional.of(new Book()));

        final var savedCommentFromCircuitBreaker = commentService.addComment(commentDto);

        then(commentRepository).should().save(any());
        assertThat(savedCommentFromCircuitBreaker).isNotNull();
        assertThat(savedCommentFromCircuitBreaker.getId()).isNull();
    }

    @Test
    void shouldReturnFalseWhenDeleteCommentIfRepositoryNotResponse() {
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, null);
        doAnswer(answersWithDelay).when(commentRepository).deleteById(anyString());

        final var result = commentService.deleteById("123");
        then(commentRepository).should().deleteById(anyString());
        assertThat(result).isFalse();
    }

}
