package ru.otus.spring.hw.service;

import java.util.Collections;
import java.util.List;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

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
    @HystrixCommand(fallbackMethod = "findAllFallbackHandler")
	public List<Genre> findAll() {
        return genreRepository.findAll();
	}

    @SuppressWarnings("unused")
    private List<Genre> findAllFallbackHandler() {
        return Collections.emptyList();
    }

	@Override
    @Transactional
    @HystrixCommand(fallbackMethod = "saveGenreFallbackHandler")
	public GenreDto saveGenre(GenreDto genreDto) {
        final var savedGenre = genreRepository.save(genreDto.toEntity());
        return new GenreDto(savedGenre);
	}

    @SuppressWarnings("unused")
    private GenreDto saveGenreFallbackHandler(GenreDto genreDto) {
        return new GenreDto();
    }

	@Override
    @Transactional
    @HystrixCommand(fallbackMethod = "deleteGenreFallbackHandler")
	public boolean deleteGenre(String id) {
        genreRepository.deleteById(id);
        return true;
	}

    @SuppressWarnings("unused")
    private boolean deleteGenreFallbackHandler(String id) {
        return false;
    }

}

