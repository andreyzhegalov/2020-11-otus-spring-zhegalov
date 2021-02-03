package ru.otus.spring.hw.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import ru.otus.spring.hw.model.Author;

public interface AuthorRepository extends MongoRepository<Author, String>, AuthorRepositoryCustom {

    Optional<Author> findByName(String name);
}
