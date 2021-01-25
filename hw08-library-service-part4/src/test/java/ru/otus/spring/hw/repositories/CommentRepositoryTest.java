package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ComponentScan("ru.otus.spring.hw.repositories")
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldReturnCorrectCommentList() {
        final var comments = commentRepository.findAll();
        assertThat(comments).hasSize(1);
    }

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
