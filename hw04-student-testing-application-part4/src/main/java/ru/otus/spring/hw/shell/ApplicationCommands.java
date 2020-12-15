package ru.otus.spring.hw.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import lombok.Setter;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.event.EventPublisher;
import ru.otus.spring.hw.event.events.CustomEvent;
import ru.otus.spring.hw.event.events.LoggingEvent;
import ru.otus.spring.hw.event.events.PrintReportEvent;
import ru.otus.spring.hw.event.events.StartQuizEvent;

@ShellComponent
public class ApplicationCommands {
    private final EventPublisher<CustomEvent> eventPublisher;

    @Setter
    private Student student;

    @Setter
    private Report report;

    public ApplicationCommands(EventPublisher<CustomEvent> eventPublisher) {
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

    private Availability isStudentExist() {
        return student == null ? Availability.unavailable("Should login first") : Availability.available();
    }

    private Availability isReportExist() {
        return report == null ? Availability.unavailable("Report not exist. Get quizzed first")
                : Availability.available();
    }
}
