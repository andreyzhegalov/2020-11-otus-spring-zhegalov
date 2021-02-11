package ru.otus.spring.hw.dto;

import javax.validation.constraints.NotBlank;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.hw.model.Comment;

@Setter
@Getter
@NoArgsConstructor
public class CommentDto {
    private String id;
    @NotBlank(message = "Please provide a comment text")
    private String text;
    @NotBlank
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
