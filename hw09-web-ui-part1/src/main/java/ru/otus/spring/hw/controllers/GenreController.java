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
import ru.otus.spring.hw.dto.GenreDto;
import ru.otus.spring.hw.service.GenreService;

@RequiredArgsConstructor
@Controller
@Validated
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genres")
    public String getList(Model model) {
        final var genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "genres";
    }

    @PostMapping("/genres")
    public String saveGenre(@Valid GenreDto genreDto, BindingResult bindingResult) {
        genreService.saveGenreDto(genreDto);
        return "redirect:/genres";
    }

    @DeleteMapping("/genres")
    public String deleteGenre(@RequestParam("id") @NotBlank String id) {
        genreService.deleteById(id);
        return "redirect:/genres";
    }
}
