package ru.otus.spring.hw.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.QuizService;
import ru.otus.spring.hw.service.front.UserService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationEventsCommands {
    private final QuizService quizService;
    private final UserService userService;
    private Student student;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public void login() {
         student = userService.getStudent();
    }

    @ShellMethod(value = "Start quiz command", key = {"s", "startQuiz"})
    @ShellMethodAvailability(value = "isStudentExist")
    public void startQuiz(){
        quizService.startTesting();
    }

    private Availability isStudentExist() {
        return student == null? Availability.unavailable("TODO"): Availability.available();
    }
}

