package ru.otus.spring.hw.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenreDto {
    private String id;
    @NotBlank
    private String name;
}
