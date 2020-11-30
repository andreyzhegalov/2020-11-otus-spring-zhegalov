package ru.otus.spring.hw.dao.mapper;

import java.util.Arrays;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

public class CsvQuestionMapper implements Mapper<String, Question> {
    private final static int LINE_ELEMENTS_COUNT = 3;
    private final static int NUMBER_POS = 0;
    private final static int QUESTION_TEXT_POS = 1;
    private final static int ANSWER_TEXT_POS = 2;

    @Override
    public Question convert(String data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("empty cvs string");
        }
        final var elementList = Arrays.asList(data.split(","));
        if (elementList.size() != LINE_ELEMENTS_COUNT) {
            throw new MapperException("wrong csv line format");
        }
        int number;
        try {
            number = Integer.parseInt(elementList.get(NUMBER_POS));
        } catch (NumberFormatException e) {
            throw new MapperException("error number in csv line");
        }
        return new Question(number, elementList.get(QUESTION_TEXT_POS), new Answer(elementList.get(ANSWER_TEXT_POS)));
    }
}
