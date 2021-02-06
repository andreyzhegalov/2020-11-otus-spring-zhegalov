package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@Getter
@Setter
@NoArgsConstructor
public class BookDto {
    private String id;
    @NotBlank
    private String title;
    @NotEmpty
    private List<String> authorsName = new ArrayList<>();
    @NotBlank
    private String genreId;
    private String genreName;

    public BookDto(@NotNull Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.authorsName = book.getAuthors().stream().map(Author::getName).collect(Collectors.toList());
        this.genreId = book.getGenre().getId();
        this.genreName = book.getGenre().getName();
    }
}
