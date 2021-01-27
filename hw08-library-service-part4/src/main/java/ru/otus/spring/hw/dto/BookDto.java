package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import ru.otus.spring.hw.model.Book;

@Getter
public class BookDto {
    private String id;
    private final String title;
    private final List<String> authorIds = new ArrayList<>();
    private final String genreId;

    public BookDto(String title, String genreId) {
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
