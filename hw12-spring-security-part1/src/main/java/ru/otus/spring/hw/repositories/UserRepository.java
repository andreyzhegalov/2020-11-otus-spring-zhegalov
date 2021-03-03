package ru.otus.spring.hw.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByName(String name);
}
