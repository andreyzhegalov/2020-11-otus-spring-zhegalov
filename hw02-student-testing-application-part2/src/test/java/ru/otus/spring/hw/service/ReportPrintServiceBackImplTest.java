package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Student;

public class ReportPrintServiceBackImplTest {
    private final Student student = new Student("Ivan", "Ivanov");
    private final Answer incorrectAnswer = new Answer("other");
    private final Question question1 = new Question(1, "question1", new Answer("1"));
    private final Question question2 = new Question(2, "question2", new Answer("2"));
    private final Question question3 = new Question(3, "question3", new Answer("3"));


    @Test
    void reportShouldContainsUser() {
        final var reportService = new ReportPrintServiceImpl();
        reportService.addAnswer(student, question1, question1.getAnswer());
        final var report = reportService.makeReport(student);

        assertThat(report.print()).contains(student.getName()).contains(student.getSecondName());
    }

    @Test
    void testPrintQuestionCount() {
        final var reportService = new ReportPrintServiceImpl();
        reportService.addAnswer(student, question1, question1.getAnswer());
        reportService.addAnswer(student, question2, question2.getAnswer());
        reportService.addAnswer(student, question3, incorrectAnswer);
        final var report = reportService.makeReport(student);

        assertThat(report.print()).contains("2").contains("3");
    }

    @Test
    void testSuccessReportForm() {
        final var reportService = new ReportPrintServiceImpl();
        reportService.addAnswer(student, question1, question1.getAnswer());
        reportService.addAnswer(student, question2, question2.getAnswer());
        reportService.addAnswer(student, question3, incorrectAnswer);
        final var report = reportService.makeReport(student);
        assertThat(report.print()).contains("!");
    }

    @Test
    void testFailReportForm() {
        final var reportService = new ReportPrintServiceImpl();
        reportService.addAnswer(student, question1, question1.getAnswer());
        reportService.addAnswer(student, question2, incorrectAnswer);
        reportService.addAnswer(student, question3, incorrectAnswer);
        final var report = reportService.makeReport(student);
        assertThat(report.print()).doesNotContain("!");
    }

    @Test
    void shouldFailReportFormIfListQuestionEmpty() {
        final var reportService = new ReportPrintServiceImpl();
        final var report = reportService.makeReport(student);
        assertThat(report.print()).doesNotContain("!");
    }
}

