package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CsvQuestionDaoTest {
    private final static String CSV_PATH = "datasets/quiz_dataset.csv";

    @Test
    void shouldThrowExceptionIfFileNotExist() {
        assertThatThrownBy(() -> new CsvQuestionDao("not_existed_file")).isInstanceOf(QuestionDaoException.class);
    }

    @Test
    void testGetFirstQuestion() {
        assertThat(new CsvQuestionDao(CSV_PATH).getFirstQuestion()).isPresent();
    }

    @Test
    void testGetFirstForEmptyDataSet() {
        assertThat(new CsvQuestionDao("datasets/empty_quiz_dataset.csv").getFirstQuestion()).isEmpty();
    }

    @Test
    void getQuestionCount() {
        assertThat(new CsvQuestionDao(CSV_PATH).getQuestionCount()).isEqualTo(5);
    }

    @Test
    void getNotExistedQuestion() {
        final var questionDao = new CsvQuestionDao(CSV_PATH);
        assertThat(questionDao.getQuestion(questionDao.getQuestionCount() + 1)).isEmpty();
    }

}
