package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.controllers.dto.GenreDto;
import ru.otus.spring.hw.model.Genre;

public interface GenreService {

    List<Genre> findAll();

    GenreDto saveGenre(GenreDto genreDto);

    boolean deleteGenre(String id);
}

