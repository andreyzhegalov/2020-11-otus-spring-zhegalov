package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Genre;

@RequiredArgsConstructor
@Service
public class IOGenreService {
    private final IOService ioService;

    private void printGenre(Genre genre) {
        String sb = "id: " + genre.getId() +
                "; " +
                "name: " + genre.getName();
        ioService.print(sb);
    }

    public void print(List<Genre> genres) {
        genres.forEach(this::printGenre);
    }
}
