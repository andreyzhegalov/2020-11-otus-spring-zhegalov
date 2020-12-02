package ru.otus.spring.hw.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Report {
    private final int threshold = 50;
    private final static Integer CORRECT_VALUE = 1;
    private final static Integer INCORRECT_VALUE = 0;
    private final static String REPORT_TEMPLATE_SUCCESS = "Congratulations to %s %s! You have been tested. Correct %d questions out of %d";
    private final static String REPORT_TEMPLATE_FAIL = "%s %s, you have not been tested. Correct %d questions out of %d";
    private final Student student;
    private final List<Integer> resultList;

    public Report(Student student) {
        Objects.requireNonNull(student);
        this.student = student;
        resultList = new ArrayList<>();
    }

    public void addAnswer(Question question, Answer answer) {
        resultList.add((question.getAnswer() == answer) ? CORRECT_VALUE : INCORRECT_VALUE);
    }

    public String print() {
        final long totalCount = resultList.size();
        final long correctAnswerCount = resultList.stream().filter(CORRECT_VALUE::equals).count();

        boolean isSuccess = false;
        if (totalCount != 0) {
            isSuccess = ((double) correctAnswerCount / totalCount * 100) > threshold;
        }
        final var template = isSuccess ? REPORT_TEMPLATE_SUCCESS : REPORT_TEMPLATE_FAIL;

        return String.format(template, student.getName(), student.getSecondName(), correctAnswerCount, totalCount);
    }
}
