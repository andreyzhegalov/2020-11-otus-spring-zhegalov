package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.GenreDto;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public void saveGenreDto(GenreDto genreDto) {
        genreRepository.save(toEntity(genreDto));
    }

    private Genre toEntity(GenreDto dto) {
        final var genre = new Genre(dto.getName());
        genre.setId(dto.getId());
        return genre;
    }

    @Override
    public void deleteById(String id) {
        genreRepository.deleteById(id);
    }

    @Override
    public List<GenreDto> findAllDto() {
        return genreRepository.findAllBy();
    }
}
