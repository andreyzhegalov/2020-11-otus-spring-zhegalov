package ru.otus.spring.hw.service;

import java.util.ArrayList;
import java.util.List;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.domain.Question;

public class FrontServiceImpl implements FrontService {
    private final IOController ioController;
    private final QuizService quizService;

    public FrontServiceImpl(QuizService quizService, IOController ioService) {
        this.quizService = quizService;
        this.ioController = ioService;
    }

    @Override
    public void printAllQuestion() {
        final var allQuestions = getAllQuestion();
        allQuestions.forEach(question -> ioController.print(question.getText()));
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
