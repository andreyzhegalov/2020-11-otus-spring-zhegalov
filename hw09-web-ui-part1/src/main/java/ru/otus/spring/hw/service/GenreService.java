package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.GenreDto;

public interface GenreService {

    void saveGenreDto(GenreDto genreDto);

    List<GenreDto> findAllDto();

    void deleteById(String id);
}
