package ru.otus.spring.hw.service.front;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

public interface FrontQuestionService {

    Answer getAnswer(Question question);

}
