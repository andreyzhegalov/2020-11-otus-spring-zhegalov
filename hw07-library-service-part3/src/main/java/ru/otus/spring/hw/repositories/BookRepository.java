package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import ru.otus.spring.hw.model.Book;

public interface BookRepository extends Repository<Book, Long> {

    @EntityGraph(attributePaths = { "genre" })
    <T> List<T> findAllBy(Class<T> type);

    @EntityGraph(attributePaths = { "genre" })
    Optional<Book> findById(Long id);

    void deleteById(Long id);

    void save(Book book);
}
