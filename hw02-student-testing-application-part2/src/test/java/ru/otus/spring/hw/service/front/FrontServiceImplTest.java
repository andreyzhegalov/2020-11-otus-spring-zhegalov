package ru.otus.spring.hw.service.front;

import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;

@ExtendWith(MockitoExtension.class)
class FrontServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private QuestionService questionService;

    @Mock
    private ReportService reportService;

    @Test
    void getStudentName() {
        new FrontServiceImpl(userService, questionService, reportService).getStudent();
        then(userService).should().getStudent();
    }

    @Test
    void getAnswer() {
        final var question = new Question(1, "text", new Answer(""));
        new FrontServiceImpl(userService, questionService, reportService).getAnswer(question);
        then(questionService).should().getAnswer(question);
    }

    @Test
    void printResult() {
        final var report = new Report("text");
        new FrontServiceImpl(userService, questionService, reportService).printResult(report);
        then(reportService).should().printResult(report);
    }
}
