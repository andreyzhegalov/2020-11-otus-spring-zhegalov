package ru.otus.spring.hw.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
import ru.otus.spring.hw.service.AuthorService;

@Controller
@RequiredArgsConstructor
@Validated
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        final var authors = authorService.findAllDto();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @PostMapping("/authors")
    public String saveAuthor(@Valid AuthorDto authorDto, BindingResult bindingResult) {
        authorService.saveAuthorDto(authorDto);
        return "redirect:/authors";
    }

    @DeleteMapping("/authors")
    public String deleteAuthor(@RequestParam("id") @NotBlank String id) {
        authorService.deleteById(id);
        return "redirect:/authors";
    }
}
