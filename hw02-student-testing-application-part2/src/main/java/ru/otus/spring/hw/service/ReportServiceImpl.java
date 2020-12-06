package ru.otus.spring.hw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

public class ReportServiceImpl implements ReportService {
    private final static int THRESHOLD = 50;
    private final static String REPORT_TEMPLATE_SUCCESS = "Congratulations to %s %s! You have been tested. Correct %d questions out of %d.";
    private final static String REPORT_TEMPLATE_FAIL = "%s %s, you have not been tested. Correct %d questions out of %d.";
    private final List<Item> database;

    @Getter
    @RequiredArgsConstructor
    private static class Item {
        private final Student student;
        private final Question question;
        private final Answer answer;
    }

    ReportServiceImpl() {
        database = new ArrayList<>();
    }

    @Override
    public void addAnswer(Student student, Question question, Answer answer) {
        database.add(new Item(student, question, answer));
    }

    @Override
    public Report makeReport(Student student) {
        final var studentItems = database.stream().filter(i -> i.getStudent().equals(student))
                .collect(Collectors.toList());
        final int totalCount = studentItems.size();
        final long correctAnswerCount = studentItems.stream()
                .filter(i -> i.getQuestion().getAnswer().equals(i.getAnswer())).count();

        boolean isSuccess = false;
        if (totalCount != 0) {
            isSuccess = ((double) correctAnswerCount / totalCount * 100) > THRESHOLD;
        }
        final var template = isSuccess ? REPORT_TEMPLATE_SUCCESS : REPORT_TEMPLATE_FAIL;
        final var text = String.format(template, student.getName(), student.getSecondName(), correctAnswerCount,
                totalCount);
        return new Report(text);
    }

}
