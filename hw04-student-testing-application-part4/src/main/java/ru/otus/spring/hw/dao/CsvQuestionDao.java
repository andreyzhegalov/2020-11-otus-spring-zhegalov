package ru.otus.spring.hw.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import ru.otus.spring.hw.dao.mapper.CsvQuestionMapper;
import ru.otus.spring.hw.domain.Question;

@Slf4j
public class CsvQuestionDao implements QuestionDao {
    private final Map<Integer, Question> questionMap;

    public CsvQuestionDao(String csvPath) {
        Objects.requireNonNull(csvPath);
        log.info("loaded file {}", csvPath);
        questionMap = load(csvPath);
    }

    @Override
    public Optional<Question> getQuestion(int number) {
        return Optional.ofNullable(questionMap.get(number));
    }

    @Override
    public List<Question> getAllQuestion() {
        return new ArrayList<>(questionMap.values());
    }

    private Map<Integer, Question> load(String csvPath) {
        final var result = new HashMap<Integer, Question>();
        try {
            try (final var inputResourceStream = Objects
                    .requireNonNull(getClass().getClassLoader().getResourceAsStream(csvPath))) {
                try (final var inputCsvStream = new BufferedReader(new InputStreamReader(inputResourceStream))) {
                    while (inputCsvStream.ready()) {
                        final var question = new CsvQuestionMapper().convert(inputCsvStream.readLine());
                        result.put(question.getNumber(), question);
                    }
                }
            }
        } catch (Exception e) {
            throw new QuestionDaoException(e);
        }
        return result;
    }
}
