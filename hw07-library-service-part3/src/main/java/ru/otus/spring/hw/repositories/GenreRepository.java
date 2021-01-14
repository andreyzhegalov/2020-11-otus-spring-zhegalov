package ru.otus.spring.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
