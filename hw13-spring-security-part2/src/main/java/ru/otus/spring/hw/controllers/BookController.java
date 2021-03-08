package ru.otus.spring.hw.controllers;

import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.dto.GenreDto;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.service.BookService;

@Controller
@RequiredArgsConstructor
@Validated
public class BookController {

    private final BookService bookService;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @GetMapping("/")
    public String startPage() {
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String listBook(Model model) {
        final var books = bookService.findAll();
        final var booksDto = books.stream().map(BookDto::new).collect(Collectors.toList());
        model.addAttribute("books", booksDto);
        model.addAttribute("authors",
                authorRepository.findAll().stream().map(AuthorDto::new).collect(Collectors.toList()));
        model.addAttribute("genres",
                genreRepository.findAll().stream().map(GenreDto::new).collect(Collectors.toList()));
        return "books";
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/books")
    public String createBook(@Valid BookDto dto, BindingResult bindingResult) {
        bookService.save(dto);
        return "redirect:/books";
    }

    @PreAuthorize("hasRole('EDITOR')")
    @DeleteMapping("/books")
    public String deleteBook(@RequestParam("id") @NotBlank String id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
