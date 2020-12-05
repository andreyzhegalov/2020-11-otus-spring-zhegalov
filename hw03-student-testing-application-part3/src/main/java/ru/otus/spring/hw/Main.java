package ru.otus.spring.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ru.otus.spring.hw.service.QuizService;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        final var context = SpringApplication.run(Main.class, args);
        final var quizService = context.getBean(QuizService.class);
        quizService.startTesting();
    }
}
