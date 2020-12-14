package ru.otus.spring.hw.service.front;

import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

@SpringBootTest
class FrontServiceImplTest {

    @MockBean
    private UserService userService;

    @MockBean
    private QuestionService questionService;

    @MockBean
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
        final var report = new Report(new Student("", ""));
        new FrontServiceImpl(userService, questionService, reportService).printResult(report);
        then(reportService).should().printResult(report);
    }
}
