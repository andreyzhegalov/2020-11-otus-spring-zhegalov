package ru.otus.spring.hw.rest;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        final var authors = authorRepository.findAll().stream().map(AuthorDto::new).collect(Collectors.toList());
        model.addAttribute("authors", authors);
        return "authors";
    }

    @PostMapping("/authors")
    public String saveAuthor(Author author) {
        if (!author.getName().trim().isEmpty()) {
            authorRepository.save(author);
        }
        return "redirect:/authors";
    }

    @DeleteMapping("/authors")
    public String deleteAuthor(@RequestParam("id") String id) {
        if (!id.trim().isEmpty()) {
            authorRepository.deleteById(id);
        }
        return "redirect:/authors";
    }
}
