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
import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.service.BookService;

@RequiredArgsConstructor
@RestController
public class BookRestController {
    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> findAll() {
        return bookService.findAll().stream().map(BookDto::new).collect(Collectors.toList());
    }

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto saveAuthor(@Valid @RequestBody BookDto bookDto) {
        return bookService.save(bookDto);
    }

    @DeleteMapping("/api/books/{id}")
    public void deleteBook(@PathVariable("id") @NotBlank String id) {
        bookService.deleteBook(id);
    }
}
