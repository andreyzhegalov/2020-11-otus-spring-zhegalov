package ru.otus.spring.hw.rest;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.service.BookService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public String startPage() {
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String listBook(Model model) {
        final var books = bookService.findAll();
        final var booksDto = books.stream().map(BookDto::new).collect(Collectors.toList());
        model.addAttribute("books", booksDto);
        return "books";
    }

    @PostMapping("/books")
    public String createBook(BookDto dto) {
        bookService.save(dto);
        return "redirect:/books";
    }

    @DeleteMapping("/books")
    public String deleteBook(@RequestParam("id") String id) {
        if (Objects.isNull(id) || id.isEmpty()) {
            return "redirect:/books";
        }
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
