package ru.otus.spring.hw.service.front;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

public interface QuestionService {

    Answer getAnswer(Question question);

}
