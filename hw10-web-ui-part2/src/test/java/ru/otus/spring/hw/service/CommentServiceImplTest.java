package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.controllers.dto.CommentDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;

@SpringBootTest
public class CommentServiceImplTest {

    @Import(CommentServiceImpl.class)
    @Configuration
    public static class TestContext {
    }

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void shouldReturnAllComments() {
        commentService.findAll();
        then(commentRepository).should().findAllDto();
    }

    @Test
    void shouldSaveNewCommentForExistedBook() {
        final var bookId = "1";
        final var book =  new Book();
        book.setId(bookId);
        final var commentDto = new CommentDto("new text", bookId);
        final var comment = new Comment();
        comment.setId("123");
        comment.setBook(book);
        given(bookRepository.findById(bookId)).willReturn(Optional.of(new Book()));
        given(commentRepository.save(any())).willReturn(comment);

        commentService.addComment(commentDto);

        then(bookRepository).should().findById(bookId);
        then(commentRepository).should().save(isA(Comment.class));
    }

    @Test
    void shouldNotSaveNewCommentIfBookNotExist() {
        final var notExistedBookId = "1";
        final var commentDto = new CommentDto("new text", notExistedBookId);
        given(bookRepository.findById(notExistedBookId)).willReturn(Optional.empty());

        assertThatCode(() -> commentService.addComment(commentDto)).isInstanceOf(ServiceException.class);

        then(bookRepository).should().findById(notExistedBookId);
        then(commentRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldDeleteCommentById() {
        final var commentId = "123";

        commentService.deleteById(commentId);
        then(commentRepository).should().deleteById(eq(commentId));
    }

    @Test
    void shouldReturnAllBookDtoById() {
        final var bookId = "123";
        commentService.findAllByBookId(bookId);
        then(commentRepository).should().findAllDtoByBookId(eq(bookId));
    }
}
