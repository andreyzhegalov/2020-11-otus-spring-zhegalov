package ru.otus.spring.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
