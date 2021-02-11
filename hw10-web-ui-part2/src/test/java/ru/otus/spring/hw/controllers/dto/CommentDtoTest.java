package ru.otus.spring.hw.controllers.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.controllers.dto.CommentDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.model.Genre;

class CommentDtoTest {

    @Test
    void shouldMakeCommentDtoFromComment() {
        final var comment = new Comment("1", "text", new Book("2", "title", new Genre("3", "name")));

        final var commentDto = new CommentDto(comment);

        assertThat(commentDto.getBookId()).isEqualTo(comment.getBook().getId());
        assertThat(commentDto.getText()).isEqualTo(comment.getText());
    }
}
