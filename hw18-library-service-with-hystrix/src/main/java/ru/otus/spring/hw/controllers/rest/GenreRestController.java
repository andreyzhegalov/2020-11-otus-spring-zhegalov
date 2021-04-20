package ru.otus.spring.hw.controllers.rest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.controllers.dto.GenreDto;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@RestController
public class GenreRestController {

    private final GenreRepository genreRepository;

    @GetMapping("/api/genres")
    @Transactional(readOnly = true)
    @HystrixCommand(fallbackMethod = "fallbackHandler")
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream().map(GenreDto::new).collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    private List<GenreDto> fallbackHandler() {
        return Collections.emptyList();
    }

    @PostMapping("/api/genres")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public GenreDto saveGenre(@Valid @RequestBody GenreDto genreDto) {
        final var savedGenre = genreRepository.save(genreDto.toEntity());
        return new GenreDto(savedGenre);
    }

    @DeleteMapping("/api/genres/{id}")
    @Transactional
    public void deleteGenre(@PathVariable("id") @NotBlank String id) {
        genreRepository.deleteById(id);
    }
}
