package ru.otus.spring.hw.controllers.dto;

import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.hw.model.Genre;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class GenreDto {
    private String id;
    @NotBlank(message = "Please provide a genre name")
    private String name;

    public GenreDto(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }

    public Genre toEntity() {
        final var genre = new Genre();
        genre.setId(getId());
        genre.setName(getName());
        return genre;
    }
}
