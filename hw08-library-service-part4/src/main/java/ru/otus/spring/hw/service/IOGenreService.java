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
        final var sb = new StringBuffer();
        sb.append("id: " + genre.getId());
        sb.append("; ");
        sb.append("name: " + genre.getName());
        ioService.print(sb.toString());
    }

    public void print(List<Genre> genres) {
        genres.forEach(g -> printGenre(g));
    }
}
