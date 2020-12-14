package ru.otus.spring.hw.service.front;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.IOLocalizedService;

@SpringBootTest
class ReportPrintServiceImplTest {
    private final static Student STUDENT = new Student("Ivan", "Ivanov");
    private final static Report REPORT = new Report(STUDENT);
    private final static Answer INCORRECT_ANSWER = new Answer("other");
    private final static Question QUESTION_1 = new Question(1, "question1", new Answer("1"));
    private final static Question QUESTION_2 = new Question(2, "question2", new Answer("2"));
    private final static Question QUESTION_3 = new Question(3, "question3", new Answer("3"));

    @Import(ReportServiceImpl.class)
    @Configuration
    public static class ReportPrintServiceImplTestInner {
    }

    @Autowired
    private ReportService reportService;

    @MockBean
    private IOLocalizedService ioLocalizeService;

    @Captor
    ArgumentCaptor<String> textCaptor;

    @Captor
    ArgumentCaptor<Object> argsCaptor;

    @Test
    void reportShouldContainsUser() {
        reportService.printResult(REPORT);
        then(ioLocalizeService).should().printLocalizedMessage(anyString(), argsCaptor.capture());
        assertThat(argsCaptor.getAllValues()).contains(STUDENT.getName()).contains(STUDENT.getSecondName());
    }

    @Test
    void testPrintQuestionCount() {
        REPORT.addAnswer(QUESTION_1, QUESTION_1.getAnswer());
        REPORT.addAnswer(QUESTION_2, QUESTION_2.getAnswer());
        REPORT.addAnswer(QUESTION_3, INCORRECT_ANSWER);

        reportService.printResult(REPORT);
        then(ioLocalizeService).should().printLocalizedMessage(anyString(), argsCaptor.capture());

        assertThat(argsCaptor.getAllValues()).contains(2).contains(3);
    }

    @Test
    void testSuccessReportForm() {
        REPORT.addAnswer(QUESTION_1, QUESTION_1.getAnswer());
        REPORT.addAnswer(QUESTION_2, QUESTION_2.getAnswer());
        REPORT.addAnswer(QUESTION_3, INCORRECT_ANSWER);

        reportService.printResult(REPORT);
        then(ioLocalizeService).should().printLocalizedMessage(textCaptor.capture(), any());

        assertThat(textCaptor.getValue()).contains("success");
    }

    @Test
    void testFailReportForm() {
        REPORT.addAnswer(QUESTION_1, QUESTION_1.getAnswer());
        REPORT.addAnswer(QUESTION_2, INCORRECT_ANSWER);
        REPORT.addAnswer(QUESTION_3, INCORRECT_ANSWER);

        new ReportServiceImpl(ioLocalizeService).printResult(REPORT);
        then(ioLocalizeService).should().printLocalizedMessage(textCaptor.capture(), any());

        assertThat(textCaptor.getValue()).contains("fail");
    }

    @Test
    void shouldFailReportFormIfListQuestionEmpty() {
        new ReportServiceImpl(ioLocalizeService).printResult(REPORT);
        then(ioLocalizeService).should().printLocalizedMessage(textCaptor.capture(), any());

        assertThat(textCaptor.getValue()).contains("fail");
    }
}
