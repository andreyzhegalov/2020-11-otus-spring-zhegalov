package ru.otus.spring.hw.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.Author;

public interface AuthorDao {

    Optional<Author> getById(long id);

    List<Author> getAll();

    long insertAuthor(Author author);

    void updateAuthor(Author author);

    void deleteAuthor(long id);

}
