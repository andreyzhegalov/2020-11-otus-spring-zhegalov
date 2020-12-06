package ru.otus.spring.hw.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Report {
    private final String text;

    public String print() {
        return text;
    }
}
