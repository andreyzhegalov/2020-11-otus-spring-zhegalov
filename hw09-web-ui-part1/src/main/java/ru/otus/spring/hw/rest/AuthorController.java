package ru.otus.spring.hw.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.repositories.AuthorRepository;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping("/")
    public String listPage(Model model) {
        final var authors = authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "list";
    }

}
