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
    void getNotExistedQuestion() {
        final var questionDao = new CsvQuestionDao(CSV_PATH);
        final int questionCount = questionDao.getAllQuestion().size();
        assertThat(questionDao.getQuestion(questionCount + 1)).isEmpty();
    }


    @Test
    void getAllQuestion(){
        final var questionDao = new CsvQuestionDao(CSV_PATH);
        assertThat(questionDao.getAllQuestion()).hasSize(5).doesNotContainNull();
    }
}
