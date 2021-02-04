package ru.otus.spring.hw.rest;

import java.util.stream.Collectors;

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
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@Controller
@RequiredArgsConstructor
@Validated
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        final var authors = authorRepository.findAll().stream().map(AuthorDto::new).collect(Collectors.toList());
        model.addAttribute("authors", authors);
        return "authors";
    }

    @PostMapping("/authors")
    public String saveAuthor(@Valid AuthorDto authorDto, BindingResult bindingResult) {
        authorRepository.save(toEntity(authorDto));
        return "redirect:/authors";
    }

    @DeleteMapping("/authors")
    public String deleteAuthor(@RequestParam("id") @NotBlank String id) {
        authorRepository.deleteById(id);
        return "redirect:/authors";
    }

    private Author toEntity(AuthorDto dto) {
        return new Author(dto.getName());
    }
}
