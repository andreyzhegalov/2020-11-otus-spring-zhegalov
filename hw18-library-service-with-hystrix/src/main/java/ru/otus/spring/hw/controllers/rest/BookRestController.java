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
import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.service.BookService;

@RequiredArgsConstructor
@RestController
public class BookRestController {
    private final BookService bookService;

    @GetMapping("/api/books")
    @HystrixCommand(fallbackMethod = "findAllFallbackHandler")
    public List<BookDto> findAll() {
        return bookService.findAll().stream().map(BookDto::new).collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    private List<BookDto> findAllFallbackHandler() {
        return Collections.emptyList();
    }

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.CREATED)
    @HystrixCommand(fallbackMethod = "saveAuthorFallbackHandler")
    public BookDto saveAuthor(@Valid @RequestBody BookDto bookDto) {
        return bookService.save(bookDto);
    }

    @SuppressWarnings("unused")
    private BookDto saveAuthorFallbackHandler(BookDto bookDto) {
        return new BookDto();
    }

    @DeleteMapping("/api/books/{id}")
    @HystrixCommand(fallbackMethod = "deleteBookFallbackHandler")
    public void deleteBook(@PathVariable("id") @NotBlank String id) {
        bookService.deleteBook(id);
    }

    @SuppressWarnings("unused")
    private void deleteBookFallbackHandler(String id){
    }
}
