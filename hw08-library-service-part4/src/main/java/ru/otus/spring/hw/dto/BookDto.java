package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.otus.spring.hw.model.Book;

@ToString
@Getter
public class BookDto {
    @Setter
    private long id;
    private final String title;
    private final List<Long> authorIds = new ArrayList<>();
    private final long genreId;

    public BookDto(String title, long genreId) {
        this.title = title;
        this.genreId = genreId;
    }

    public BookDto(@NotNull Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        book.getAuthors().forEach(a -> authorIds.add(a.getId()));
        this.genreId = book.getGenre().getId();
    }
}
