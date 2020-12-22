package ru.otus.spring.hw.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.Genre;

public interface GenreDao {

    Optional<Genre> getById(long id);

    List<Genre> getAll();

    long insertGenre(Genre genre);

    void updateGenre(Genre genre);

    long insertOrUpdate(Genre genre);

    void deleteGenre(long id);
}
