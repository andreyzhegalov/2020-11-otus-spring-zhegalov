package ru.otus.spring.hw.dto;

import org.jetbrains.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.hw.model.Comment;

@Data
@NoArgsConstructor
public class CommentDto {
    private String id;
    private String text;
    private String bookId;
    private String bookTitle;

    public CommentDto(String text, String bookId) {
        this.text = text;
        this.bookId = bookId;
    }

    public CommentDto(@NotNull Comment comment) {
        this.text = comment.getText();
        this.bookId = comment.getBook().getId();
    }
}
