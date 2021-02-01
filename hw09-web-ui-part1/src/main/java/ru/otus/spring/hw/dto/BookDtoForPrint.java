package ru.otus.spring.hw.dto;

import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@Getter
public class BookDtoForPrint {
    private final String id;
    private final String title;
    private final String authorsName;
    private final String genreName;

    public BookDtoForPrint(@NotNull Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.authorsName = book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(",")).toString();
        this.genreName = book.getGenre().getName();
    }
}
