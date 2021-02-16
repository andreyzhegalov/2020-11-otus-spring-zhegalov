package ru.otus.spring.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;
import ru.otus.spring.hw.model.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String>, AuthorRepositoryCustom {

    Mono<Author> findByName(String name);

    Mono<Author> findById(String id);
}
