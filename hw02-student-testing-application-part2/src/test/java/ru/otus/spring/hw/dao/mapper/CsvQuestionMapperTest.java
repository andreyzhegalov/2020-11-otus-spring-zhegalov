package ru.otus.spring.hw.dao.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.domain.Answer;

class CsvQuestionMapperTest {

    @Test
    void shouldThrowExceptionForEmptyLine() {
        final var emptyCsvLine = "";
        assertThatThrownBy(() -> new CsvQuestionMapper().convert(emptyCsvLine))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionForWrongCsvLineFormat() {
        final var csvLineWithoutAnswer = "1,question_text";
        assertThatThrownBy(() -> new CsvQuestionMapper().convert(csvLineWithoutAnswer))
                .isInstanceOf(MapperException.class);
    }

    @Test
    void shouldThrowExceptionWhenNumberErrorParse() {
        final var csvLineWithErrorNumber = "number,question_text,answer";
        assertThatThrownBy(() -> new CsvQuestionMapper().convert(csvLineWithErrorNumber))
                .isInstanceOf(MapperException.class);
    }

    @Test
    void convertFromCsvShouldReturnQuestion() {
        final var correctCsvLine = "1,question_text,answer_text";
        final var question = new CsvQuestionMapper().convert(correctCsvLine);
        assertThat(question.getNumber()).isEqualTo(1);
        assertThat(question.getText()).isEqualTo("question_text");
        // assertThat(question.getAnswer()).isEqualTo(new Answer("answer_text"));
    }

}
