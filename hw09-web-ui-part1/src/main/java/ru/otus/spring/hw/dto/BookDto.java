package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private final static String SEPARATOR = ",";
    private String id;
    private String title;
    private List<String> authorsName = new ArrayList<>();
    private String genreName;

    public String getAuthorsName() {
        return String.join(SEPARATOR, authorsName);
    }

    public void setAuthorsName(String authorsNames) {
        this.authorsName = Arrays.stream(authorsNames.split(SEPARATOR)).map(String::trim).collect(Collectors.toList());
    }

    public List<String> getAuthorsNameList() {
        return authorsName;
    }

    public BookDto(@NotNull Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.authorsName = book.getAuthors().stream().map(Author::getName).collect(Collectors.toList());
        this.genreName = book.getGenre().getName();
    }
}
