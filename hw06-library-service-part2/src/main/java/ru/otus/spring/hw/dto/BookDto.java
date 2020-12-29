package ru.otus.spring.hw.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class BookDto {
    @Setter
    @Getter
    private long id;
    @Getter
    private final String title;
    @Getter
    private final long authorId;
    @Getter
    private final long genreId;

    public BookDto(String title, long authorId, long genreId) {
        this.id = 0;
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
    }

    public BookDto(long id, String title, long authorId, long genreId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
    }
}
