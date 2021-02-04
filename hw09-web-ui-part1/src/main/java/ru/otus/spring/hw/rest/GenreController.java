package ru.otus.spring.hw.rest;

import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.GenreDto;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Controller
@Validated
public class GenreController {
    private final GenreRepository genreRepository;

    @GetMapping("/genres")
    public String getList(Model model) {
        final var genres = genreRepository.findAll();
        model.addAttribute("genres", genres);
        return "genres";
    }

    @PostMapping("/genres")
    public String saveGenre(@Validated GenreDto genreDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final var error = bindingResult.getAllErrors().stream().map(ObjectError::toString)
                    .collect(Collectors.joining(", "));
            throw new ControllerException(error);
        }
        genreRepository.save(toEntity(genreDto));
        return "redirect:/genres";
    }

    @DeleteMapping("/genres")
    public String deleteGenre(@RequestParam("id") @NotBlank String id) {
        genreRepository.deleteById(id);
        return "redirect:/genres";
    }

    private Genre toEntity(GenreDto dto) {
        return new Genre(dto.getName());
    }

}
