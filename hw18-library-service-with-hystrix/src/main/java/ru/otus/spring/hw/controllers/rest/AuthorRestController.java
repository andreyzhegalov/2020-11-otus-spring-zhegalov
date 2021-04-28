package ru.otus.spring.hw.controllers.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.controllers.dto.AuthorDto;
import ru.otus.spring.hw.service.AuthorService;

@RequiredArgsConstructor
@RestController
public class AuthorRestController {
    private final AuthorService authorService;

    @GetMapping("api/authors")
    public List<AuthorDto> getAllAuthors() {
        return authorService.findAll().stream().map(AuthorDto::new).collect(Collectors.toList());
    }


    @PostMapping("/api/authors")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto saveAuthor(@Valid @RequestBody AuthorDto authorDto) {
        return authorService.saveAuthor(authorDto);
    }


    @DeleteMapping("/api/authors/{id}")
    public void deleteAuthor(@PathVariable("id") @NotBlank String id) {
        authorService.deleteAuthor(id);
    }
}
