package ru.otus.spring.hw.service.front;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

public class FrontQuestionServiceImpl implements FrontQuestionService {
    private final IOController ioController;

    public FrontQuestionServiceImpl(IOController ioController) {
        this.ioController = ioController;
    }

    @Override
    public Answer getAnswer(Question question) {
        ioController.print(question.getText());
        final var answerText = ioController.read();
        return new Answer(answerText);
    }

}
