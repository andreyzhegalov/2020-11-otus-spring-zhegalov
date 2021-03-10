package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.spring.hw.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Transactional(readOnly = true)
    @NotNull
    List<Genre> findAll();

    @Transactional(readOnly = true)
    @NotNull
    Optional<Genre> findById(@NotNull Long id);
}
