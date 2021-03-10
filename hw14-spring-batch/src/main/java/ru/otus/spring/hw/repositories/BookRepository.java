package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.spring.hw.model.Book;

public interface BookRepository extends Repository<Book, Long> {

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = { "genre" })
    List<Book> findAll();

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = { "genre" })
    Optional<Book> findById(Long id);
}
