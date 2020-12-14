package ru.otus.spring.hw.service.front;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.IOLocalizedService;

@ExtendWith(MockitoExtension.class)
class ReportPrintServiceImplTest {
    private final Student student = new Student("Ivan", "Ivanov");
    private final Report report = new Report(student);
    private final Answer incorrectAnswer = new Answer("other");
    private final Question question1 = new Question(1, "question1", new Answer("1"));
    private final Question question2 = new Question(2, "question2", new Answer("2"));
    private final Question question3 = new Question(3, "question3", new Answer("3"));

    @Mock
    private IOLocalizedService ioLocalizeService;

    @Captor
    ArgumentCaptor<String> textCaptor;

    @Captor
    ArgumentCaptor<Object> argsCaptor;

    @Test
    void reportShouldContainsUser() {
        new ReportServiceImpl(ioLocalizeService).printResult(report);
        then(ioLocalizeService).should().printLocalizedMessage(anyString(), argsCaptor.capture());
        assertThat(argsCaptor.getAllValues()).contains(student.getName()).contains(student.getSecondName());
    }

    @Test
    void testPrintQuestionCount() {
        report.addAnswer(question1, question1.getAnswer());
        report.addAnswer(question2, question2.getAnswer());
        report.addAnswer(question3, incorrectAnswer);

        new ReportServiceImpl(ioLocalizeService).printResult(report);
        then(ioLocalizeService).should().printLocalizedMessage(anyString(), argsCaptor.capture());

        assertThat(argsCaptor.getAllValues()).contains(2).contains(3);
    }

    @Test
    void testSuccessReportForm() {
        report.addAnswer(question1, question1.getAnswer());
        report.addAnswer(question2, question2.getAnswer());
        report.addAnswer(question3, incorrectAnswer);

        new ReportServiceImpl(ioLocalizeService).printResult(report);
        then(ioLocalizeService).should().printLocalizedMessage(textCaptor.capture(), any());

        assertThat(textCaptor.getValue()).contains("success");
    }

    @Test
    void testFailReportForm() {
        report.addAnswer(question1, question1.getAnswer());
        report.addAnswer(question2, incorrectAnswer);
        report.addAnswer(question3, incorrectAnswer);

        new ReportServiceImpl(ioLocalizeService).printResult(report);
        then(ioLocalizeService).should().printLocalizedMessage(textCaptor.capture(), any());

        assertThat(textCaptor.getValue()).contains("fail");
    }

    @Test
    void shouldFailReportFormIfListQuestionEmpty() {
        new ReportServiceImpl(ioLocalizeService).printResult(report);
        then(ioLocalizeService).should().printLocalizedMessage(textCaptor.capture(), any());

        assertThat(textCaptor.getValue()).contains("fail");
    }
}
