package ru.otus.spring.hw.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.QuizService;
import ru.otus.spring.hw.service.front.ReportService;
import ru.otus.spring.hw.service.front.UserService;

@ShellComponent
public class ApplicationEventsCommands {
    private final QuizService quizService;
    private final UserService userService;
    private final ReportService reportService;
    private Student student;
    private Report report;

    public ApplicationEventsCommands(QuizService quizService, UserService userService, ReportService reportService){
        this.quizService = quizService;
        this.userService = userService;
        this.reportService = reportService;
    }


    @ShellMethod(value = "Login command", key = {"l", "login"})
    public void login() {
         student = userService.getStudent();
    }

    @ShellMethod(value = "Start quiz command", key = {"s", "startQuiz"})
    @ShellMethodAvailability(value = "isStudentExist")
    public void startQuiz(){
        this.report = quizService.startTesting( this.student);
    }

    @ShellMethod(value = "Print report command", key = {"p", "printReport"})
    @ShellMethodAvailability(value = "isReportExist")
    public void printReport(){
        reportService.printResult(report);
    }

    private Availability isStudentExist() {
        return student == null? Availability.unavailable("Should login first"): Availability.available();
    }

    private Availability isReportExist(){
        return report == null ? Availability.unavailable("Report not exist. Get quizzed first"): Availability.available();
    }
}

