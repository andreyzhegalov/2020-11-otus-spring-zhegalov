package ru.otus.spring.hw.controllers.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
    public List<GenreDto> findAll() {
        return null;
        // return genreRepository.findAll().stream().map(GenreDto::new).collect(Collectors.toList());
    }

    @PostMapping("/api/genres")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public GenreDto saveGenre(@Valid @RequestBody GenreDto genreDto) {
        return null;
        // final var savedGenre = genreRepository.save(genreDto.toEntity());
        // return new GenreDto(savedGenre);
    }

    @DeleteMapping("/api/genres/{id}")
    @Transactional
    public void deleteGenre(@PathVariable("id") @NotBlank String id) {
        genreRepository.deleteById(id);
    }
}
