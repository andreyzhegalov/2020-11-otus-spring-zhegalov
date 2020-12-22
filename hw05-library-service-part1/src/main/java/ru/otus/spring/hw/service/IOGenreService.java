package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Genre;

@RequiredArgsConstructor
@Service
public class IOGenreService implements IOModelService<Genre> {
    private final IOService ioService;

    @Override
    public void print(List<Genre> genres) {
        genres.forEach(b -> ioService.print(b.toString()));
    }

    @Override
    public Genre get() {
        throw new UnsupportedOperationException();
    }

}
