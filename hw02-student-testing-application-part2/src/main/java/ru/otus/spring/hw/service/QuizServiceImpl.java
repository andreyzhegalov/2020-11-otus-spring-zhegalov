package ru.otus.spring.hw.service;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.service.front.FrontService;

@Service
public class QuizServiceImpl implements QuizService {
    private final FrontService frontService;
    private final QuestionDao questionDao;
    private final ReportService reportService;

    public QuizServiceImpl(QuestionDao questionDao, FrontService frontService, ReportService reportService) {
        this.questionDao = questionDao;
        this.frontService = frontService;
        this.reportService = reportService;
    }

    @Override
    public void startTesting() {
        final var student = frontService.getStudent();
        final var questionList = questionDao.getAllQuestion();
        questionList.forEach(q -> {
            final var answer = frontService.getAnswer(q);
            reportService.addAnswer(student, q, answer);
        });

        final var report = reportService.makeReport(student);
        frontService.printResult(report);
    }

    @Override
    public void printAllQuestion() {
        final var questionList = questionDao.getAllQuestion();
        questionList.forEach(frontService::getAnswer);
    }
}
