package ru.otus.spring.hw.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.Author;

public interface AuthorDao {

    Optional<Author> getById(long id);

    List<Author> getAll();

    long insertAuthor(Author book);

    void updateAuthor(Author book);

    void insertOrUpdate(Author book);

    void deleteAuthor(long id);

}
