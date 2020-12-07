package ru.otus.spring.hw.service;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

public interface ReportPrintService {
    void addAnswer(Student student, Question question, Answer answer);

    Report makeReport(Student student);
}
