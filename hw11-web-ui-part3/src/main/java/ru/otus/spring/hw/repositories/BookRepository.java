package ru.otus.spring.hw.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.model.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    boolean existsBookByAuthors_id(String authorId);

    boolean existsBookByGenre_id(String genreId);

    Optional<Book> findByTitle(String name);
}
