package ru.otus.spring.hw.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.otus.spring.hw.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = { "genre" })
    List<Book> findAll();


}
