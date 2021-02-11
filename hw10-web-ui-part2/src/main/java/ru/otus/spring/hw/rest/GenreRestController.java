package ru.otus.spring.hw.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.GenreDto;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@RestController
public class GenreRestController {

    private final GenreRepository genreRepository;

    @CrossOrigin
    @GetMapping("/api/genres")
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream().map(GenreDto::new).collect(Collectors.toList());
    }

    @CrossOrigin
    @PostMapping("/api/genres")
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDto saveGenre(@Valid @RequestBody GenreDto genreDto) {
        final var savedGenre = genreRepository.save(genreDto.toEntity());
        return new GenreDto(savedGenre);
    }

    @CrossOrigin
    @DeleteMapping("/api/genres/{id}")
    public void deleteGenre(@PathVariable("id") @NotBlank String id) {
        genreRepository.deleteById(id);
    }
}
