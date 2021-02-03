package ru.otus.spring.hw.rest;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@Controller
public class EditAuthorController {
    private final AuthorRepository authorRepository;

    @GetMapping(value = { "/edit_author/{id}", "/edit_author" })
    public String editAuthorById(@PathVariable Optional<String> id, Model model) {
        model.addAttribute("authorDto", new AuthorDto());
        id.ifPresent(i -> authorRepository.findById(i).ifPresent(e -> model.addAttribute("editAuthor", e)));
        return "edit_author";
    }

    @PostMapping("/edit_author")
    public String saveAuthor(@Valid AuthorDto authorDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit_author";
        }
        authorRepository.save(toEntity(authorDto));
        return "redirect:/authors";
    }

    private Author toEntity(AuthorDto dto) {
        final var author = new Author();
        author.setName(dto.getName());
        author.setId(dto.getId());
        return author;
    }
}
