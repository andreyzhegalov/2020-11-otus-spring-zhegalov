package ru.otus.spring.hw.rest;

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
import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@RestController
public class AuthorRestController {

    private final AuthorRepository authorRepository;

    @GetMapping("api/authors")
    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream().map(AuthorDto::new).collect(Collectors.toList());
    }

    @PostMapping("/api/authors")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto saveAuthor(@Valid @RequestBody AuthorDto authorDto) {
        final var savedAuthor = authorRepository.save(authorDto.toEntity());
        return new AuthorDto(savedAuthor);
    }

    @DeleteMapping("/api/authors/{id}")
    public void deleteAuthor(@PathVariable("id") @NotBlank String id) {
        authorRepository.deleteById(id);
    }
}
