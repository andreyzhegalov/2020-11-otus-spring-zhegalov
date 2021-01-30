package ru.otus.spring.hw.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import ru.otus.spring.hw.model.Book;

public interface BookRepository extends MongoRepository<Book, String> {

    boolean existsBookByAuthors_id(String authorId);

    Optional<Book> findByTitle(String name);
}
