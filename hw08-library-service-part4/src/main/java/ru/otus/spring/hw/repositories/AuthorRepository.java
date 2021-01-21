package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import ru.otus.spring.hw.model.Author;

public interface AuthorRepository {

    @NotNull
    List<Author> findAll();

    @NotNull
    Optional<Author> findById(@NotNull Long id);
}
