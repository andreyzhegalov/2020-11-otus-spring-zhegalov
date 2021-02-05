package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.GenreDto;
import ru.otus.spring.hw.model.Genre;

public interface GenreService {

    void saveGenreDto(GenreDto genreDto);

    List<Genre> findAll();

    void deleteById(String id);
}
