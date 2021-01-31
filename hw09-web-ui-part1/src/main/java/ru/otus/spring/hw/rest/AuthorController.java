package ru.otus.spring.hw.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping("/author")
    public String listPage(Model model) {
        final var authors = authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "author";
    }

    @PostMapping("/author")
    public String saveAuthor(Author author) {
        authorRepository.save(author);
        return "redirect:/author";
    }

    @DeleteMapping("/author")
    public String deleteAuthor(@RequestParam("id") String id) {
        authorRepository.deleteById(id);
        return "redirect:/author";
    }
}
