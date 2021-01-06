package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentDto {
    private final String text;
    private final long bookId;
    private final List<Long> bookIds = new ArrayList<>();
}
