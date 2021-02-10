package ru.otus.spring.hw.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

}
