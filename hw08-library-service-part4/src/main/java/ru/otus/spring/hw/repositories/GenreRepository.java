package ru.otus.spring.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.model.Genre;

@Repository
public interface GenreRepository extends MongoRepository<Genre, String> {
}
