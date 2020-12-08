package ru.otus.spring.hw.service.front;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import ru.otus.spring.hw.config.AppProps;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.IOService;

@ExtendWith(MockitoExtension.class)
class ReportPrintServiceImplTest {
    private final Student student = new Student("Ivan", "Ivanov");
    private final Report report = new Report(student);
    private final Answer incorrectAnswer = new Answer("other");
    private final Question question1 = new Question(1, "question1", new Answer("1"));
    private final Question question2 = new Question(2, "question2", new Answer("2"));
    private final Question question3 = new Question(3, "question3", new Answer("3"));

    @Mock
    private IOService ioService;

    @Mock
    private MessageSource messageSource;

    private AppProps props;

    @Captor
            
    ArgumentCaptor<String> textCaptor;

    @Captor
    ArgumentCaptor<Object[]> argsCaptor;

    @BeforeEach
    void setUp() {
        props = new AppProps();
        props.setLocale(Locale.US);
    }

    @Test
    void reportShouldBeSendingToIOService() {
        final var text = "report";
        given(messageSource.getMessage(anyString(), any(), any())).willReturn(text);
        new ReportServiceImpl(ioService, messageSource, props).printResult(report);
        then(ioService).should().print(text);
    }

    @Test
    void reportShouldContainsUser() {
        report.addAnswer(question1, question1.getAnswer());

        new ReportServiceImpl(ioService, messageSource, props).printResult(report);
        then(messageSource).should().getMessage(anyString(), argsCaptor.capture(), eq(props.getLocale()));

        assertThat(argsCaptor.getValue()).contains(student.getName(), student.getSecondName());
    }

    @Test
    void testPrintQuestionCount() {
        report.addAnswer(question1, question1.getAnswer());
        report.addAnswer(question2, question2.getAnswer());
        report.addAnswer(question3, incorrectAnswer);

        new ReportServiceImpl(ioService, messageSource, props).printResult(report);
        then(messageSource).should().getMessage(anyString(), argsCaptor.capture(), eq(props.getLocale()));

        assertThat(argsCaptor.getValue()).contains(2).contains(3);
    }

    @Test
    void testSuccessReportForm() {
        report.addAnswer(question1, question1.getAnswer());
        report.addAnswer(question2, question2.getAnswer());
        report.addAnswer(question3, incorrectAnswer);

        new ReportServiceImpl(ioService, messageSource, props).printResult(report);
        then(messageSource).should().getMessage(textCaptor.capture(), any(), eq(props.getLocale()));

        assertThat(textCaptor.getValue()).contains("success");
    }

    @Test
    void testFailReportForm() {
        report.addAnswer(question1, question1.getAnswer());
        report.addAnswer(question2, incorrectAnswer);
        report.addAnswer(question3, incorrectAnswer);

        new ReportServiceImpl(ioService, messageSource, props).printResult(report);
        then(messageSource).should().getMessage(textCaptor.capture(), any(), eq(props.getLocale()));

        assertThat(textCaptor.getValue()).contains("fail");
    }

    @Test
    void shouldFailReportFormIfListQuestionEmpty() {
        new ReportServiceImpl(ioService, messageSource, props).printResult(report);
        then(messageSource).should().getMessage(textCaptor.capture(), any(), eq(props.getLocale()));

        assertThat(textCaptor.getValue()).contains("fail");
    }
}
