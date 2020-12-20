package ru.otus.spring.hw.service.front;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

@Service
@RequiredArgsConstructor
public class FrontServiceImpl implements FrontService {
    private final UserService userService;
    private final QuestionService questionService;
    private final ReportService reportService;

    @Override
    public Answer getAnswer(final Question question) {
        return questionService.getAnswer(question);
    }

    @Override
    public Student getStudent() {
        return userService.getStudent();
    }

    @Override
    public void printResult(Report report) {
        reportService.printResult(report);
    }
}
