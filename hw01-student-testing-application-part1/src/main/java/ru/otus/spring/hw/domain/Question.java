package ru.otus.spring.hw.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Question {
    private final int number;
    private final String text;
    private final Answer answer;
}
