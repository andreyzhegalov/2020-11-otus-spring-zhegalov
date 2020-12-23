package ru.otus.spring.hw.service.io;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;

@RequiredArgsConstructor
@Service
public class IOAuthorService {
    private final IOService ioService;

    public void print(List<Author> authors) {
        authors.forEach(b -> ioService.print(b.toString()));
    }

    public Author get() {
        throw new UnsupportedOperationException();
    }
}
