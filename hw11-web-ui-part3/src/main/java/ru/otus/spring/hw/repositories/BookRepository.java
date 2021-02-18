package ru.otus.spring.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import ru.otus.spring.hw.model.Book;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Mono<Boolean> existsBookByAuthors_id(String authorId);

    Mono<Boolean> existsBookByGenre_id(String genreId);

    Mono<Book> findByTitle(String name);
}
