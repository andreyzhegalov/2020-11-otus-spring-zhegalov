package ru.otus.spring.hw.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.service.BookService;

@RequiredArgsConstructor
@RestController
public class BookRestController {
    private final BookService bookService;

    @CrossOrigin
    @GetMapping("/api/books")
    public List<BookDto> findAll() {
        return bookService.findAll().stream().map(BookDto::new).collect(Collectors.toList());
    }

}
