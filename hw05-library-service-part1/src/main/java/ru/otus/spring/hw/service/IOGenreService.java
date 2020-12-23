package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Genre;

@RequiredArgsConstructor
@Service
public class IOGenreService {
    private final IOService ioService;

    public void print(List<Genre> genres) {
        genres.forEach(b -> ioService.print(b.toString()));
    }

    public Genre get() {
        throw new UnsupportedOperationException();
    }

}
