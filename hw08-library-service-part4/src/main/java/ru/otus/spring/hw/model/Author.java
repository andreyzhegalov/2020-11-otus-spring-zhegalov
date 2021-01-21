package ru.otus.spring.hw.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    private String id;
    private String name;

    public Author(String name) {
        this.name = name;
    }
}
