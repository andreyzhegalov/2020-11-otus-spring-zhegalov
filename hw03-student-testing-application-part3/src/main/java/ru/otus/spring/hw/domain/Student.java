package ru.otus.spring.hw.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Student {
    final String name;
    final String secondName;

    @Override
    public String toString() {
        return name + " " + secondName;
    }
}
