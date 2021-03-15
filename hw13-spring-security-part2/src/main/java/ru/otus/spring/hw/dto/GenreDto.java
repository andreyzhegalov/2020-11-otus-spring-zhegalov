package ru.otus.spring.hw.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.hw.model.Genre;

@Getter
@Setter
@NoArgsConstructor
public class GenreDto {
    private String id;
    @NotBlank
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
