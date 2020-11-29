package ru.otus.spring.hw.service;

import java.util.ArrayList;
import java.util.List;

import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.front.Front;

public class FrontServiceImpl implements FrontService {
    private final Front front;
    private final QuizService quizService;

    public FrontServiceImpl(QuizService quizService, Front front) {
        this.quizService = quizService;
        this.front = front;
    }

    @Override
    public void printAllQuestion() {
        final var allQuestions = getAllQuestion();
        allQuestions.stream().forEach(question -> front.print(question.getText()));
    }

    private List<Question> getAllQuestion() {
        final var resultList = new ArrayList<Question>();
        var mayBeQuestion = quizService.getNextQuestion(null);
        while (mayBeQuestion.isPresent()) {
            resultList.add(mayBeQuestion.get());
            mayBeQuestion = quizService.getNextQuestion(mayBeQuestion.get());
            if (mayBeQuestion.isEmpty()) {
                break;
            }
        }
        return resultList;
    }
}
