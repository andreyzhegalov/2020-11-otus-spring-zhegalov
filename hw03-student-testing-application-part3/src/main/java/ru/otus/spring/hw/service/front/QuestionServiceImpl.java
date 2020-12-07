package ru.otus.spring.hw.service.front;

import ru.otus.spring.hw.service.IOService;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final IOService ioService;

    public QuestionServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Answer getAnswer(Question question) {
        ioService.print(question.getText());
        final var answerText = ioService.read();
        return new Answer(answerText);
    }

}
