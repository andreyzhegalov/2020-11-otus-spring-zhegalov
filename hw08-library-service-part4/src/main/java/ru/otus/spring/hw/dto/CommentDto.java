package ru.otus.spring.hw.dto;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.otus.spring.hw.model.Comment;

@Getter
@RequiredArgsConstructor
@ToString
public class CommentDto {
    private final String text;
    private final String bookId;

    public CommentDto(@NotNull Comment comment) {
        this.text = comment.getText();
        this.bookId = comment.getBook().getId();
    }
}
