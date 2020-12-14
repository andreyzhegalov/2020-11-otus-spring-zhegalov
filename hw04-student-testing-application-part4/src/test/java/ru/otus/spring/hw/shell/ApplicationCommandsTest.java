package ru.otus.spring.hw.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;

import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.QuizService;
import ru.otus.spring.hw.service.front.ReportService;
import ru.otus.spring.hw.service.front.UserService;

@SpringBootTest
class ApplicationCommandsTest {

    @MockBean
    private UserService userService;

    @MockBean
    private QuizService quizService;

    @MockBean
    private ReportService reportService;

    @Autowired
    private Shell shell;

    @Test
    void loginShouldInvokeGetStudent() {
        shell.evaluate(() -> "l");
        then(userService).should().getStudent();
    }

    @Test
    void testingShouldNotStartIfTheStudentIsNotLoggedIn() {
        final var res = shell.evaluate(() -> "s");
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void startTestingShouldBeInvokedAfterCommandStart() {
        final var student = new Student("ivan", "ivanov");
        given(userService.getStudent()).willReturn(student);

        shell.evaluate(() -> "l");
        shell.evaluate(() -> "s");

        then(quizService).should().startTesting(eq(student));
    }

    @Test
    void reportPrintingShouldNotStartIfTheTestFails() {
        final var res = shell.evaluate(() -> "p");
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void theReportShouldBePrintedAfterTheCommandPrint() {
        final var student = new Student("ivan", "ivanov");
        final var report = new Report(student);
        given(userService.getStudent()).willReturn(student);
        given(quizService.startTesting(any())).willReturn(report);

        shell.evaluate(() -> "l");
        shell.evaluate(() -> "s");
        shell.evaluate(() -> "p");

        then(reportService).should().printResult(report);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void reportShouldNotBePrintedForNewUser() {
        final var student = new Student("ivan", "ivanov");
        final var newStudent = new Student("ivan", "neivanov");
        final var report = new Report(student);
        given(userService.getStudent()).willReturn(student).willReturn(newStudent);
        given(quizService.startTesting(any())).willReturn(report);

        shell.evaluate(() -> "l");
        shell.evaluate(() -> "s");
        shell.evaluate(() -> "l");
        final var res = shell.evaluate(() -> "p");
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

}
