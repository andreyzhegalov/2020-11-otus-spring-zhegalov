package ru.otus.spring.hw.controllers.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.controllers.dto.GenreDto;
import ru.otus.spring.hw.service.GenreService;

@RequiredArgsConstructor
@RestController
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public List<GenreDto> findAll() {
        return genreService.findAll().stream().map(GenreDto::new).collect(Collectors.toList());
    }

    @PostMapping("/api/genres")
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDto saveGenre(@Valid @RequestBody GenreDto genreDto) {
        return genreService.saveGenre(genreDto);
    }

    @DeleteMapping("/api/genres/{id}")
    public void deleteGenre(@PathVariable("id") @NotBlank String id) {
        genreService.deleteGenre(id);
    }
}
