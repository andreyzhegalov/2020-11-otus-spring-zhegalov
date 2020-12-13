package ru.otus.spring.hw.service.front;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.service.IOLocalizedService;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final IOLocalizedService ioLocalizesService;

    @Override
    public Answer getAnswer(Question question) {
        ioLocalizesService.print(question.getText());
        final var answerText = ioLocalizesService.read();
        return new Answer(answerText);
    }
}
