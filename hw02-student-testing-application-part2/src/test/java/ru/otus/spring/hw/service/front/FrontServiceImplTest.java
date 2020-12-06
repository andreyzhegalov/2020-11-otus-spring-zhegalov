package ru.otus.spring.hw.service.front;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

@ExtendWith(MockitoExtension.class)
class FrontServiceImplTest {

    @Mock
    private FrontUserService frontUserService;

    @Mock
    private FrontQuestionService frontQuestionService;

    @Mock
    private FrontReportService frontReportService;

    @Test
    void getStudentName() {
        given(frontUserService.getStudent()).willReturn(new Student("ivan", "ivanov"));
        new FrontServiceImpl(frontUserService, frontQuestionService, frontReportService).getStudent();
        then(frontUserService).should().getStudent();
    }

    @Test
    void getAnswer() {
        final var question = new Question(1, "text", new Answer(""));
        new FrontServiceImpl(frontUserService, frontQuestionService, frontReportService).getAnswer(question);
        then(frontQuestionService).should().getAnswer(any());
    }

    @Test
    void printResult() {
        final var report = new Report(new Student("ivan", "ivanov"));
        new FrontServiceImpl(frontUserService, frontQuestionService, frontReportService).printResult(report);
        then(frontReportService).should().printResult(report);
    }
}
