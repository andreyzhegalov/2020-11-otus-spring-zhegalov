package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.otus.spring.hw.model.Book;

@EqualsAndHashCode
@ToString
public class BookDto {
    @Setter
    @Getter
    private long id;
    @Getter
    private final String title;
    @Getter
    private final List<Long> authorIds = new ArrayList<>();
    @Getter
    private final long genreId;

    public BookDto(String title, long genreId) {
        this.id = 0;
        this.title = title;
        this.genreId = genreId;
    }

    public BookDto(long id, String title, long genreId) {
        this.id = id;
        this.title = title;
        this.genreId = genreId;
    }

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        book.getAuthors().forEach(a -> {
            authorIds.add(a.getId());
        });
        this.genreId = book.getGenre().getId();
    }
}
