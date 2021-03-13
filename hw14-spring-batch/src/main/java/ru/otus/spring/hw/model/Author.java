package ru.otus.spring.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private final static long NOT_EXISTED_ID = 0L;

    private long id = NOT_EXISTED_ID;
    private String name;
}
