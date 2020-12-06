package ru.otus.spring.hw.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ru.otus.spring.hw.dao.mapper.CsvQuestionMapper;
import ru.otus.spring.hw.domain.Question;

public class CsvQuestionDao implements QuestionDao {
    private final Map<Integer, Question> questionMap;
    private final QuestionLocalizer questionLocalizer;

    public CsvQuestionDao(String csvPath, QuestionLocalizer questionLocalizer) {
        Objects.requireNonNull(csvPath);
        Objects.requireNonNull(questionLocalizer);

        this.questionLocalizer = questionLocalizer;

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

        prepareQuestionsText();
    }

    @Override
    public Optional<Question> getQuestion(int number) {
        return Optional.ofNullable(questionMap.get(number));
    }

    @Override
    public List<Question> getAllQuestion() {
        return new ArrayList<>(questionMap.values());
    }

    private void prepareQuestionsText(){
        questionMap.values().forEach(q -> {
            q.setText( questionLocalizer.getQuestionText( q.getNumber() ));
        });
    }
}
