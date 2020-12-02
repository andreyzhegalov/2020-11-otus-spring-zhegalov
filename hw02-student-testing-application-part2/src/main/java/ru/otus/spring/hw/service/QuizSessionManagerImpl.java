package ru.otus.spring.hw.service;

import ru.otus.spring.hw.domain.Question;

public class QuizSessionManagerImpl implements QuizSessionManager {
    private final FrontService frontService;
    private final QuizService quizService;

    public QuizSessionManagerImpl(FrontService frontService, QuizService quizService) {
        this.frontService = frontService;
        this.quizService = quizService;
    }

    @Override
    public void startTesting() {
        final var student = frontService.getStudent();
        Question newQuestion = null;
        while (true) {
            final var mayBeNewQuestion = quizService.getNextQuestion(newQuestion);
            if (mayBeNewQuestion.isEmpty()) {
                break;
            }
            newQuestion = mayBeNewQuestion.get();
            final var answer = frontService.getAnswer(newQuestion);
        }
        frontService.printResult();
    }
}
