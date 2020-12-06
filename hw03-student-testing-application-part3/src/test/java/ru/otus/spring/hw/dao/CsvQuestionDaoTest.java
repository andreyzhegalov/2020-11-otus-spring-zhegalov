package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {
    private final static String CSV_PATH = "datasets/quiz_dataset.csv";

    @Mock
    private QuestionLocalizer questionLocalizer;

    @Test
    void shouldThrowExceptionIfFileNotExist() {
        assertThatThrownBy(() -> new CsvQuestionDao("not_existed_file", questionLocalizer)).isInstanceOf(QuestionDaoException.class);
    }

    @Test
    void getNotExistedQuestion() {
        final var questionDao = new CsvQuestionDao(CSV_PATH, questionLocalizer);
        final int questionCount = questionDao.getAllQuestion().size();
        assertThat(questionDao.getQuestion(questionCount + 1)).isEmpty();
    }


    @Test
    void getAllQuestion(){
        final var questionDao = new CsvQuestionDao(CSV_PATH, questionLocalizer);
        assertThat(questionDao.getAllQuestion()).hasSize(5).doesNotContainNull();
        then(questionLocalizer).should(times(5)).getQuestionText(anyInt());
    }
}
