package ru.otus.spring.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ru.otus.spring.hw.service.QuizService;

@Configuration
@ComponentScan
public class Main {

    public static void main(String[] args) {
        final ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        final var quizService = context.getBean(QuizService.class);
        quizService.startTesting();
    }
}
