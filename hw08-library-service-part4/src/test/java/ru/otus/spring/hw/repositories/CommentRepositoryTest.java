package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;

import ru.otus.spring.hw.dto.CommentDto;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ComponentScan("ru.otus.spring.hw.repositories")
public class CommentRepositoryTest {
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
        final var commentList = commentRepository.findAll();
        final var commentDtoList = commentRepository.findAllDto();
        assertThat(commentDtoList).isNotEmpty().doesNotContainNull().allMatch(c -> c.getClass() == CommentDto.class)
                .allMatch(c -> Objects.nonNull(c.getBookId()));

        final var commentDto = commentDtoList.get(0);
        assertThat(bookRepository.findById(commentDto.getBookId())).isNotEmpty();
    }
}
