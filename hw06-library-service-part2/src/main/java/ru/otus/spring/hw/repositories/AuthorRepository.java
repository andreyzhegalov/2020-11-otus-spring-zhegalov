package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.Author;

public interface AuthorRepository {

    List<Author> findAll();

    Optional<Author> findById(long id);

    Author save(Author author);

    void remove(long id);
}
