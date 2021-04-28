package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.controllers.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;

public interface AuthorService {

    List<Author> findAll();

    AuthorDto saveAuthor(AuthorDto authorDto);

    boolean deleteAuthor(String id);
}

