package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;

public interface AuthorService {

    void saveAuthorDto(AuthorDto authorDto);

    List<Author> findAll();

    void deleteById(String id);
}
