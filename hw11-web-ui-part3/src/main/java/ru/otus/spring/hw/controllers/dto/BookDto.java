package ru.otus.spring.hw.controllers.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.jetbrains.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.hw.model.Book;

@Data
@NoArgsConstructor
public class BookDto {
    private String id;
    @NotBlank(message = "Please provide a book title")
    private String title;
    private List<String> authorsName = new ArrayList<>();
    @NotEmpty(message = "Please provide a authors")
    private List<String> authorsId = new ArrayList<>();
    @NotBlank(message = "Please provide a genre")
    private String genreId;
    private String genreName;

    public BookDto(@NotNull Book book) {
        if (Objects.nonNull(book.getId())) {
            this.id = book.getId();
        }
        this.title = book.getTitle();
        book.getAuthors().forEach(a -> {
            this.authorsId.add(a.getId());
            this.authorsName.add(a.getName());
        });
        this.genreId = book.getGenre().getId();
        this.genreName = book.getGenre().getName();
    }
}
