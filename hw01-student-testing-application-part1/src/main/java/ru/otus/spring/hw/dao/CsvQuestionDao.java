package ru.otus.spring.hw.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ru.otus.spring.hw.dao.mapper.CsvQuestionMapper;
import ru.otus.spring.hw.domain.Question;

public class CsvQuestionDao implements QuestionDao {
    private final Map<Integer, Question> questionMap;

    public CsvQuestionDao(String csvPath) {
        Objects.requireNonNull(csvPath);

        questionMap = new HashMap<>();
        try {
            final var inputStream = getClass().getClassLoader().getResource(csvPath).openStream();
            final var in = new BufferedReader(new InputStreamReader(inputStream));
            loadData(in);
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

    private void loadData(BufferedReader in) throws IOException {
        while (in.ready()) {
            final var question = new CsvQuestionMapper().convert(in.readLine());
            questionMap.put(question.getNumber(), question);
        }
    }

    @Override
    public Optional<Question> getFirstQuestion() {
        final var entrySet = questionMap.entrySet();
        if (entrySet.isEmpty()) {
            return Optional.empty();
        }
        final var firstValue = entrySet.iterator().next().getValue();
        return Optional.of(firstValue);
    }
}
