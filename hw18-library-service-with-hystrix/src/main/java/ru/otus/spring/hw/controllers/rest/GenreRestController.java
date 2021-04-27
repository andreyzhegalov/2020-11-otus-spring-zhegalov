package ru.otus.spring.hw.controllers.rest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

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
    @HystrixCommand(fallbackMethod = "findAllFallbackHandler")
    public List<GenreDto> findAll() {
        return genreService.findAll().stream().map(GenreDto::new).collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    private List<GenreDto> findAllFallbackHandler() {
        return Collections.emptyList();
    }

    @PostMapping("/api/genres")
    @ResponseStatus(HttpStatus.CREATED)
    @HystrixCommand(fallbackMethod = "saveGenreFallbackHandler")
    public GenreDto saveGenre(@Valid @RequestBody GenreDto genreDto) {
        return genreService.saveGenre(genreDto);
    }

    @SuppressWarnings("unused")
    private GenreDto saveGenreFallbackHandler(GenreDto genreDto) {
        return new GenreDto();
    }

    @DeleteMapping("/api/genres/{id}")
    @HystrixCommand(fallbackMethod = "deleteGenreFallbackHandler")
    public void deleteGenre(@PathVariable("id") @NotBlank String id) {
        genreService.deleteGenre(id);
    }

    @SuppressWarnings("unused")
    private void deleteGenreFallbackHandler(String id) {
    }
}
