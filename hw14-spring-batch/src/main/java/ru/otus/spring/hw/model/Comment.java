package ru.otus.spring.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private final static long NOT_EXISTED_ID = 0L;

    private long id = NOT_EXISTED_ID;
    private String text;
    private Book book;
}
