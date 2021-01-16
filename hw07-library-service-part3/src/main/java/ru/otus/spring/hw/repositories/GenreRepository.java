package ru.otus.spring.hw.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.spring.hw.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Transactional(readOnly = true)
    List<Genre> findAll();
}
