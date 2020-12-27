package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.Genre;

public interface GenreRepository {

    List<Genre> findAll();

    Optional<Genre> findById(long id);

    Genre save(Genre genre);

    void remove(long id);
}
