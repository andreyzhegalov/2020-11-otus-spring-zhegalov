package ru.otus.spring.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import ru.otus.spring.hw.model.Genre;

@Repository
public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    Mono<Genre> findByName(String name);
}
