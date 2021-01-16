package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.spring.hw.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Transactional(readOnly = true)
    @NotNull
    List<Author> findAll();

    @Transactional(readOnly = true)
    @NotNull
    Optional<Author> findById(@NotNull Long id);
}
