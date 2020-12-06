package ru.otus.spring.hw.service.front;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

@Service
public class FrontServiceImpl implements FrontService {
    private final FrontUserService frontUserService;
    private final FrontQuestionService frontQuestionService;
    private final FrontReportService frontReportService;

    public FrontServiceImpl(FrontUserService frontUserService, FrontQuestionService frontQuestionService,
            FrontReportService frontReportService) {
        this.frontUserService = frontUserService;
        this.frontQuestionService = frontQuestionService;
        this.frontReportService = frontReportService;
    }

    @Override
    public Answer getAnswer(final Question question) {
        return frontQuestionService.getAnswer(question);
    }

    @Override
    public Student getStudent() {
        return frontUserService.getStudent();
    }

    @Override
    public void printResult(Report report) {
        frontReportService.printResult(report);
    }
}
