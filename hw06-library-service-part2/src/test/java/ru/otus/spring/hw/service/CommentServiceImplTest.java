package ru.otus.spring.hw.service;

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

import ru.otus.spring.hw.dto.CommentDto;
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
        then(commentRepository).should().findAll();
    }

    @Test
    void shouldSaveNewCommentForExistedBook() {
        final var bookId = 1L;
        final var commentDto = new CommentDto("new text", bookId);
        given(bookRepository.findById(bookId)).willReturn(Optional.of(new Book()));

        commentService.addComment(commentDto);

        then(bookRepository).should().findById(bookId);
        then(commentRepository).should().save(isA(Comment.class));
    }

    @Test
    void shouldDeleteAllCommentsForBook() {
        final var bookId = 1L;

        commentService.deleteByBookId(bookId);

        then(commentRepository).should().deleteByBookId(eq(bookId));
    }
}
