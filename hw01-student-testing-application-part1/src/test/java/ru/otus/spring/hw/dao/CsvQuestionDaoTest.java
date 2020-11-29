package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CsvQuestionDaoTest {
    private String csvPath;

    @BeforeEach
    void setUp() throws URISyntaxException {
        final var ulrCsvPath = getClass().getClassLoader().getResource("datasets/quiz_dataset.csv");
        csvPath = ulrCsvPath.toString();
    }

    @Test
    void testGetFirstQuestion(){
        assertThat(new CsvQuestionDao(csvPath).getFirstQuestion()).isPresent();
    }

    @Test
    void shouldThrowExceptionIfFileNotExist() {
        assertThatThrownBy(() -> new CsvQuestionDao("not_existed_file")).isInstanceOf(QuestionDaoException.class);
    }

    @Test
    void getQuestionCount() {
        assertThat(new CsvQuestionDao(csvPath).getQuestionCount()).isEqualTo(5);
    }

    @Test
    void getNotExistedQuestion() {
        final var questionDao = new CsvQuestionDao(csvPath);
        assertThat(questionDao.getQuestion(questionDao.getQuestionCount() + 1)).isEmpty();
    }

    @Test
    void getExistedQuestion() {
    }
}
