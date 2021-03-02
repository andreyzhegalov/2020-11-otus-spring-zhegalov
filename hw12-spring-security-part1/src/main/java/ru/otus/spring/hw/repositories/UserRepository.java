package ru.otus.spring.hw.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import ru.otus.spring.hw.security.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByName(String name);
}

