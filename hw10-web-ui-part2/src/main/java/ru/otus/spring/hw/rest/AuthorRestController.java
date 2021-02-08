package ru.otus.spring.hw.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@RestController
public class AuthorRestController {

    private final AuthorRepository authorRepository;

    @CrossOrigin
    @GetMapping("api/authors")
    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream().map(AuthorDto::new).collect(Collectors.toList());
    }

    @CrossOrigin
    @PostMapping("/api/authors")
    public void saveAuthor(@Valid AuthorDto authorDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return;
        }
        authorRepository.save(authorDto.toEntity());
    }
}
