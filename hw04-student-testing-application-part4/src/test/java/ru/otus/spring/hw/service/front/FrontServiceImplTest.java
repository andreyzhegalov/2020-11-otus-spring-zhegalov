package ru.otus.spring.hw.service.front;

import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

@SpringBootTest
class FrontServiceImplTest {

    @Import(FrontServiceImpl.class)
    @Configuration
    public static class FrontServiceTestInner {
    }

    @MockBean
    private UserService userService;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private ReportService reportService;

    @Autowired
    private FrontService frontService;

    @Test
    void getStudentName() {
        frontService.getStudent();
        then(userService).should().getStudent();
    }

    @Test
    void getAnswer() {
        final var question = new Question(1, "text", new Answer(""));
        frontService.getAnswer(question);
        then(questionService).should().getAnswer(question);
    }

    @Test
    void printResult() {
        final var report = new Report(new Student("", ""));
        frontService.printResult(report);
        then(reportService).should().printResult(report);
    }
}
