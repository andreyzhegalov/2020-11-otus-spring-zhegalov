package ru.otus.spring.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.otus.spring.hw.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
