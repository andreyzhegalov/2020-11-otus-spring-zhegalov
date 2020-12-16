package ru.otus.spring.hw.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;

import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.event.EventPublisher;
import ru.otus.spring.hw.event.events.AbstractCustomEvent;
import ru.otus.spring.hw.event.events.LoggingEvent;
import ru.otus.spring.hw.event.events.PrintReportEvent;
import ru.otus.spring.hw.event.events.StartQuizEvent;

@SpringBootTest
class ApplicationCommandsTest {

    @Autowired
    private Shell shell;

    @Autowired
    private ApplicationCommands applicationCommands;

    @MockBean
    private EventPublisher<AbstractCustomEvent> eventPublisher;

    @Captor
    ArgumentCaptor<AbstractCustomEvent> captor;

    @Test
    void loginShouldPublishLoginEvent() {
        shell.evaluate(() -> "l");

        then(eventPublisher).should().publish(captor.capture());
        assertThat(captor.getValue()).isInstanceOf(LoggingEvent.class);
    }

    @Test
    void testingShouldNotStartIfTheStudentIsNotLoggedIn() {
        final var res = shell.evaluate(() -> "s");
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void startTestingShouldBeInvokedStartQuizEvent() {
        final var student = new Student("ivan", "ivanov");
        applicationCommands.setStudent(student);

        shell.evaluate(() -> "s");

        then(eventPublisher).should().publish(captor.capture());
        assertThat(captor.getValue()).isInstanceOf(StartQuizEvent.class);
    }

    @Test
    void reportPrintingShouldNotStartWithOutReport() {
        final var res = shell.evaluate(() -> "p");
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void theReportShouldBePrintedAfterTheCommandPrint() {
        final var student = new Student("ivan", "ivanov");
        applicationCommands.setReport(new Report(student));

        shell.evaluate(() -> "p");

        then(eventPublisher).should().publish(captor.capture());
        assertThat(captor.getValue()).isInstanceOf(PrintReportEvent.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void reportShouldNotBePrintedForNewUser() {
        final var student = new Student("ivan", "ivanov");
        final var report = new Report(student);

        applicationCommands.setStudent(student);
        applicationCommands.setReport(report);

        shell.evaluate(() -> "l");
        final var res = shell.evaluate(() -> "p");
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

}
