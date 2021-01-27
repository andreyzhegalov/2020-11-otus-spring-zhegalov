package ru.otus.spring.hw.dto;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.otus.spring.hw.model.Comment;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CommentDto {
    private String id = new String();
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
