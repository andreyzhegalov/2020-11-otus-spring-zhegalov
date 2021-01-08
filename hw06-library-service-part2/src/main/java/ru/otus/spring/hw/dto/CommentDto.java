package ru.otus.spring.hw.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.otus.spring.hw.model.Comment;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class CommentDto {
    private final String text;
    private final long bookId;

    public CommentDto(Comment comment) {
        this.text = comment.getText();
        this.bookId = comment.getBook().getId();
    }
}
