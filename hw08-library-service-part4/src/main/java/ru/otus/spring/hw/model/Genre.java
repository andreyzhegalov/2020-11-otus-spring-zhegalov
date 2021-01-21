package ru.otus.spring.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre {

    private long id;

    private String name;
}
