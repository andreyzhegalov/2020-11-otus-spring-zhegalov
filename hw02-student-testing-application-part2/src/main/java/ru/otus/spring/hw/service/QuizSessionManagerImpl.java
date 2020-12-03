package ru.otus.spring.hw.service;

import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;

public class QuizSessionManagerImpl implements QuizSessionManager {
    private final FrontService frontService;
    private final QuizService quizService;
    private final QuestionDao questionDao;

    public QuizSessionManagerImpl(QuestionDao questionDao, FrontService frontService, QuizService quizService) {
        this.questionDao = questionDao;
        this.frontService = frontService;
        this.quizService = quizService;
    }

    @Override
    public void startTesting() {
        final var student = frontService.getStudent();
        final var report = new Report(student);
        Question newQuestion = null;
        while (true) {
            final var mayBeNewQuestion = quizService.getNextQuestion(newQuestion);
            if (mayBeNewQuestion.isEmpty()) {
                break;
            }
            newQuestion = mayBeNewQuestion.get();
            final var answer = frontService.getAnswer(newQuestion);
            report.addAnswer(newQuestion, answer);
        }
        frontService.printResult(report);
    }

    public void startTesting1() {
        final var student = frontService.getStudent();
        final var report = new Report(student);
        final var questionList = questionDao.getAllQuestion();
        questionList.forEach(q->
                {
                    final var answer = frontService.getAnswer(q);
                    report.addAnswer(q, answer);
                }
        );
        frontService.printResult(report);
    }
}
