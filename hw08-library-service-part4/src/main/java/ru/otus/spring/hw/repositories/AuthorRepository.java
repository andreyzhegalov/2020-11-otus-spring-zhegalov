 package ru.otus.spring.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import ru.otus.spring.hw.model.Author;

public interface AuthorRepository extends MongoRepository<Author, String>{
}
