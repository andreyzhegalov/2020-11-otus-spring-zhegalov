package ru.otus.spring.hw.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class BookDto {
    @Getter
    private final long id;
    @Getter
    private final String title;
    @Getter
    private final long authorId;

    public BookDto(String title, long authorId) {
        this.id = 0;
        this.title = title;
        this.authorId = authorId;
    }

    public BookDto(long id, String title, long authorId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
    }
}
