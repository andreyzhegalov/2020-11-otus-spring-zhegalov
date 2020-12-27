package ru.otus.spring.hw.repositories;

import java.util.List;

import ru.otus.spring.hw.model.Author;

public interface AuthorRepository {

    List<Author> findAll();
}

