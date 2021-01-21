package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.model.Genre;

@Repository
public interface GenreRepository {

    @NotNull
    List<Genre> findAll();

    @NotNull
    Optional<Genre> findById(@NotNull Long id);
}
