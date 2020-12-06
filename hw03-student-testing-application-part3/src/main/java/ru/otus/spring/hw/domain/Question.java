package ru.otus.spring.hw.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Question {
    private final int number;
    private String text;
    private final Answer answer;

    public void setText(String text){
        this.text = text;
    }
}
