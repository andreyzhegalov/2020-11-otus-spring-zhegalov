package ru.otus.spring.hw.rest;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.BookDtoForPrint;
import ru.otus.spring.hw.dto.BookDtoInput;
import ru.otus.spring.hw.service.BookService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public String startPage() {
        return "redirect:/book";
    }

    @GetMapping("/book")
    public String listBook(Model model) {
        final var books = bookService.findAll();
        final var booksDto = books.stream().map(BookDtoForPrint::new).collect(Collectors.toList());
        model.addAttribute("books", booksDto);
        return "book";
    }

    @PostMapping("/book")
    public String create(BookDtoInput dto) {
        bookService.save(dto);
        return "redirect:/book";
    }

    @DeleteMapping("/book")
    public String deleteAuthor(@RequestParam("id") String id) {
        bookService.deleteBook(id);
        return "redirect:/book";
    }
}