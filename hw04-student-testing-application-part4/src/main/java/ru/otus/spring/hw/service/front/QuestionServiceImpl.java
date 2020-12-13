package ru.otus.spring.hw.service.front;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.service.IOService;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final IOService ioService;

    @Override
    public Answer getAnswer(Question question) {
        ioService.print(question.getText());
        final var answerText = ioService.read();
        return new Answer(answerText);
    }
}
