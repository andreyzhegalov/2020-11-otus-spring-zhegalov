package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDtoInput {

    private String id;
    private String title;
    private List<String> authorsName = new ArrayList<>();
    private String genreName;

    public void setAuthorsName(String authorsNames) {
        this.authorsName = Arrays.stream(authorsNames.split(",")).map(String::trim).collect(Collectors.toList());
    }
}
