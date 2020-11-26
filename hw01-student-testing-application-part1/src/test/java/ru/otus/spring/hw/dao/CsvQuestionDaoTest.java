package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CsvQuestionDaoTest {

    @Test
    void getQuestion() {
        assertThat(new CsvQuestionDao().getQuestion(0)).isEmpty();
    }
}
