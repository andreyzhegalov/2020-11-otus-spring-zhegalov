package ru.otus.spring.hw.shell;

import org.springframework.context.event.EventListener;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.event.EventPublisher;
import ru.otus.spring.hw.event.events.AbstractCustomEvent;
import ru.otus.spring.hw.event.events.LoggingEvent;
import ru.otus.spring.hw.event.events.PrintReportEvent;
import ru.otus.spring.hw.event.events.ReportEvent;
import ru.otus.spring.hw.event.events.StartQuizEvent;
import ru.otus.spring.hw.event.events.UserLoggingEvent;

@ShellComponent
public class ApplicationCommands {
    private final EventPublisher<AbstractCustomEvent> eventPublisher;

    private Student student;

    private Report report;

    public ApplicationCommands(EventPublisher<AbstractCustomEvent> eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @ShellMethod(value = "Login command", key = { "l", "login" })
    public void login() {
        eventPublisher.publish(new LoggingEvent(this));
        report = null;
    }

    @ShellMethod(value = "Start quiz command", key = { "s", "start-quiz" })
    @ShellMethodAvailability(value = "isStudentExist")
    public void startQuiz() {
        eventPublisher.publish(new StartQuizEvent(this, student));
    }

    @ShellMethod(value = "Print report command", key = { "p", "print-report" })
    @ShellMethodAvailability(value = "isReportExist")
    public void printReport() {
        eventPublisher.publish(new PrintReportEvent(this, report));
    }

    @EventListener
    public void onUserLoggingEvent(UserLoggingEvent newEvent) {
        this.student = newEvent.getStudent();
    }

    @EventListener
    public void onReportEvent(ReportEvent event) {
        this.report = event.getReport();
    }

    private Availability isStudentExist() {
        return student == null ? Availability.unavailable("Should login first") : Availability.available();
    }

    private Availability isReportExist() {
        return report == null ? Availability.unavailable("Report not exist. Get quizzed first")
                : Availability.available();
    }
}
