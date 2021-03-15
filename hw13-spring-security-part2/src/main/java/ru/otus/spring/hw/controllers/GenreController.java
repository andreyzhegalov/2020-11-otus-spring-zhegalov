package ru.otus.spring.hw.controllers;

import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.GenreDto;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Controller
@Validated
public class GenreController {
    private final GenreRepository genreRepository;

    @GetMapping("/genres")
    public String getList(Model model) {
        final var genres = genreRepository.findAll().stream().map(GenreDto::new).collect(Collectors.toList());
        model.addAttribute("genres", genres);
        return "genres";
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/genres")
    public String saveGenre(@Valid GenreDto genreDto, BindingResult bindingResult) {
        genreRepository.save(genreDto.toEntity());
        return "redirect:/genres";
    }

    @PreAuthorize("hasRole('EDITOR')")
    @DeleteMapping("/genres")
    public String deleteGenre(@RequestParam("id") @NotBlank String id) {
        genreRepository.deleteById(id);
        return "redirect:/genres";
    }
}
