package ru.otus.spring.hw.service.io;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;

@RequiredArgsConstructor
@Service
public class IOAuthorService implements IOModelService<Author> {
    private final IOService ioService;

    @Override
    public void print(List<Author> authors) {
        authors.forEach(b -> ioService.print(b.toString()));
    }

    @Override
    public Author get() {
        throw new UnsupportedOperationException();
    }
}
