package ru.otus.spring.hw.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import ru.otus.spring.hw.dao.mapper.CsvQuestionMapper;
import ru.otus.spring.hw.domain.Question;

public class CsvQuestionDao implements QuestionDao {
    private final static int FIRST_NUMBER_VALUE = 1;
    private final Map<Integer, Question> questionMap;

    public CsvQuestionDao(String csvPath) {
        Objects.requireNonNull(csvPath);

        questionMap = new HashMap<>();
        try {

            try (final var inputResourceStream = Objects
                    .requireNonNull(getClass().getClassLoader().getResourceAsStream(csvPath))) {
                try (final var inputCsvStream = new BufferedReader(new InputStreamReader(inputResourceStream))) {
                    while (inputCsvStream.ready()) {
                        final var question = new CsvQuestionMapper().convert(inputCsvStream.readLine());
                        questionMap.put(question.getNumber(), question);
                    }
                }
            }

        } catch (Exception e) {
            throw new QuestionDaoException(e.toString());
        }
    }

    @Override
    public Optional<Question> getQuestion(int number) {
        return Optional.ofNullable(questionMap.get(number));
    }

    public static int getFirstQuestionNumber() {
        return FIRST_NUMBER_VALUE;
    }

    public int getQuestionCount() {
        return questionMap.size();
    }

    @Override
    public List<Question> getAllQuestion() {
        return new ArrayList<>(questionMap.values());
    }
}
