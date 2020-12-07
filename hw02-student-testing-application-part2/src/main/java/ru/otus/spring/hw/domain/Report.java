package ru.otus.spring.hw.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
public class Report {
    final Student student;
    final List<Pair> result;

    @Getter
    @RequiredArgsConstructor
    public static class Pair {
        private final Question question;
        private final Answer answer;
    }

    public Report(Student student) {
        Objects.requireNonNull(student);
        this.student = student;
        this.result = new ArrayList<Pair>();
    }

    public void addAnswer(Question question, Answer answer) {
        result.add(new Pair(question, answer));
    }

}
