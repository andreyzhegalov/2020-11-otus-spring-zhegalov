package ru.otus.spring.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentRepositoryTest {
    private static final int COMMENT_COUNT = 2;
    private static final long EXISTED_COMMENT_ID = 1L;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldReturnCommentDtoForAllCommentsInOneQuery() {
    }

    @Test
    void shouldReturnCommentByIdWhenCommentExistedInTwoQuery() {
    }

    @Test
    void shouldDeleteCommentAfterBookWasDeleted() {
    }
}
