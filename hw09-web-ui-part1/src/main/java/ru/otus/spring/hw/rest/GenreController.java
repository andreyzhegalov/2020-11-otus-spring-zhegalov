package ru.otus.spring.hw.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Controller
public class GenreController {
    private final GenreRepository genreRepository;

    @GetMapping("/genres")
    public String getList(Model model) {
        final var genres = genreRepository.findAll();
        model.addAttribute("genres", genres);
        return "genres";
    }

    @PostMapping("/genres")
    public String saveGenre(Genre genres) {
        genreRepository.save(genres);
        return "redirect:/genres";
    }

    @DeleteMapping("/genres")
    public String deleteGenre(@RequestParam("id") String id) {
        genreRepository.deleteById(id);
        return "redirect:/genres";
    }
}
