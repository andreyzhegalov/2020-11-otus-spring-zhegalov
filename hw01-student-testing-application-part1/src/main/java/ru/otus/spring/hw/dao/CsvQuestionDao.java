package ru.otus.spring.hw.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import ru.otus.spring.hw.dao.mapper.CsvQuestionMapper;
import ru.otus.spring.hw.domain.Question;

public class CsvQuestionDao implements QuestionDao {
    private final Map<Integer, Question> questionMap;

    public CsvQuestionDao(String csvPath) {
        Objects.requireNonNull(csvPath);
        final var ulrCsvPath = getClass().getClassLoader().getResource(csvPath);
        questionMap = new HashMap<>();
        try {
            loadData(Path.of(ulrCsvPath.toURI()));
        } catch (Exception e) {
            throw new QuestionDaoException(e.toString());
        }
    }

    @Override
    public Optional<Question> getQuestion(int number) {
        return Optional.ofNullable(questionMap.get(number));
    }

    public int getQuestionCount() {
        return questionMap.size();
    }

    private void loadData(Path csvPath) throws IOException {
        try (final var scanner = new Scanner(csvPath)) {
            while (scanner.hasNextLine()) {
                final var question = new CsvQuestionMapper().convert(scanner.nextLine());
                questionMap.put(question.getNumber(), question);
            }
        }
    }

    @Override
    public Optional<Question> getFirstQuestion() {
        final var firstValue = questionMap.entrySet().iterator().next().getValue();
        return Optional.of(firstValue);
    }
}
