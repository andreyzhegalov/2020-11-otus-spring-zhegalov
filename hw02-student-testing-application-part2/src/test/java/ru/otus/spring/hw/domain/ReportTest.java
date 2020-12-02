package ru.otus.spring.hw.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReportTest {
    private final Student student = new Student("Ivan", "Ivanov");
    private final Answer incorrectAnswer = new Answer("other");
    private final Question question1 = new Question(1, "question1", new Answer("1"));
    private final Question question2 = new Question(2, "question2", new Answer("2"));
    private final Question question3 = new Question(3, "question3", new Answer("3"));

    @Test
    void testPrintUser() {
        assertThat(new Report(student).print()).contains(student.getName()).contains(student.getSecondName());
    }

    @Test
    void testPrintQuestionCount() {
        final var report = new Report(student);
        report.addAnswer(question1, question1.getAnswer());
        report.addAnswer(question2, question2.getAnswer());
        report.addAnswer(question3, incorrectAnswer);

        assertThat(report.print()).contains("2").contains("3");
    }

    @Test
    void testSuccessReportForm() {
        final var report = new Report(student);
        report.addAnswer(question1, question1.getAnswer());
        report.addAnswer(question2, question2.getAnswer());
        report.addAnswer(question3, incorrectAnswer);

        assertThat(report.print()).contains("!");
    }

    @Test
    void testFailReportForm() {
        final var report = new Report(student);
        report.addAnswer(question1, question1.getAnswer());
        report.addAnswer(question2, incorrectAnswer);
        report.addAnswer(question3, incorrectAnswer);

        assertThat(report.print()).doesNotContain("!");
    }

    @Test
    void shouldFailReportFormIfListQuestionEmpty() {
        final var report = new Report(student);
        assertThat(report.print()).doesNotContain("!");
    }

}
