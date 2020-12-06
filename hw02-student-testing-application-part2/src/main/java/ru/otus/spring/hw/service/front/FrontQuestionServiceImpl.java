package ru.otus.spring.hw.service.front;

import ru.otus.spring.hw.service.IOService;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

@Service
public class FrontQuestionServiceImpl implements FrontQuestionService {
    private final IOService ioService;

    public FrontQuestionServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Answer getAnswer(Question question) {
        ioService.print(question.getText());
        final var answerText = ioService.read();
        return new Answer(answerText);
    }

}
