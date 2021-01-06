package ru.otus.spring.hw.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentDto {
    private final String text;
    private final long bookId;
}
