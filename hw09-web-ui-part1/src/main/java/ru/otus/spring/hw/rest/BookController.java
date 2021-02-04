package ru.otus.spring.hw.rest;

import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.service.BookService;

@Controller
@RequiredArgsConstructor
@Validated
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
    public String createBook(@Validated BookDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final var error = bindingResult.getAllErrors().stream().map(ObjectError::toString)
                    .collect(Collectors.joining(", "));
            throw new ControllerException(error);
        }
        bookService.save(dto);
        return "redirect:/books";
    }

    @DeleteMapping("/books")
    public String deleteBook(@RequestParam("id") @NotBlank String id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
