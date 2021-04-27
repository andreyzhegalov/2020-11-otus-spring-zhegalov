package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.controllers.dto.GenreDto;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.GenreRepository;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;

	@Override
    @Transactional(readOnly = true)
	public List<Genre> findAll() {
        return genreRepository.findAll();
	}

	@Override
    @Transactional
	public GenreDto saveGenre(GenreDto genreDto) {
        final var savedGenre = genreRepository.save(genreDto.toEntity());
        return new GenreDto(savedGenre);
	}

	@Override
    @Transactional
	public void deleteGenre(String id) {
        genreRepository.deleteById(id);
	}

}

