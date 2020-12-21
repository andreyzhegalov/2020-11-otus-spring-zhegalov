package ru.otus.spring.hw.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.dto.AuthorDto;


public interface AuthorDao {

    Optional<AuthorDto> getById(long id);

    List<AuthorDto> getAll();

    long insertAuthor(AuthorDto book);

    void updateAuthor(AuthorDto book);

    void insertOrUpdate(AuthorDto book);

    void deleteAuthor(long id);

}
