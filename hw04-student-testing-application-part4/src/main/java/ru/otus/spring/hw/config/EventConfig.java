package ru.otus.spring.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.event.EventManager;
import ru.otus.spring.hw.event.EventManagerImpl;
import ru.otus.spring.hw.event.EventPublisher;
import ru.otus.spring.hw.event.events.CustomEvent;
import ru.otus.spring.hw.event.events.LoggingEvent;
import ru.otus.spring.hw.event.events.PrintReportEvent;
import ru.otus.spring.hw.event.events.ReportEvent;
import ru.otus.spring.hw.event.events.StartQuizEvent;
import ru.otus.spring.hw.event.events.UserLoggingEvent;
import ru.otus.spring.hw.service.QuizService;
import ru.otus.spring.hw.service.front.ReportService;
import ru.otus.spring.hw.service.front.UserService;
import ru.otus.spring.hw.shell.ApplicationCommands;

@Configuration
public class EventConfig {

    // @Bean(name = "applicationEventMulticaster")
    // public ApplicationEventMulticaster applicationEventMulticaster() {
    // SimpleApplicationEventMulticaster eventMulticaster = new
    // SimpleApplicationEventMulticaster();
    // eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
    // return eventMulticaster;
    // }
    //
    @Bean
    EventManager<CustomEvent> eventManager(EventPublisher<CustomEvent> eventPublisher, UserService userService,
            ApplicationCommands applicationCommands, QuizService quizService, ReportService reportService) {
        final var eventManager = new EventManagerImpl();

        eventManager.connect(LoggingEvent.class, e -> {
            final var newStudent = userService.getStudent();
            eventPublisher.publish(new UserLoggingEvent(this, newStudent));
        });

        eventManager.connect(UserLoggingEvent.class, e -> {
            applicationCommands.setStudent((Student) e.getPayload());
        });

        eventManager.connect(StartQuizEvent.class, e -> {
            final var report = quizService.startTesting((Student) e.getPayload());
            eventPublisher.publish(new ReportEvent(this, report));
        });

        eventManager.connect(ReportEvent.class, e -> {
            applicationCommands.setReport((Report) e.getPayload());
        });

        eventManager.connect(PrintReportEvent.class, e ->{
            reportService.printResult((Report)e.getPayload());
        });

        return eventManager;
    }

}
