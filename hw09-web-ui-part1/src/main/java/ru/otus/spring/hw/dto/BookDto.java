package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.hw.model.Book;

@Getter
@Setter
@NoArgsConstructor
public class BookDto {
    private String id;
    @NotBlank
    private String title;
    private List<String> authorsName = new ArrayList<>();
    @NotEmpty
    private List<String> authorsId = new ArrayList<>();
    @NotBlank
    private String genreId;
    private String genreName;

    public BookDto(@NotNull Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        book.getAuthors().forEach(a -> {
            this.authorsId.add(a.getId());
            this.authorsName.add(a.getName());
        });
        this.genreId = book.getGenre().getId();
        this.genreName = book.getGenre().getName();
    }
}
