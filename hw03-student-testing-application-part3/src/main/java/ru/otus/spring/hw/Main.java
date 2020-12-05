package ru.otus.spring.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ru.otus.spring.hw.config.DataSourceConfig;
import ru.otus.spring.hw.service.QuizService;

@EnableConfigurationProperties(DataSourceConfig.class )
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        final var context = SpringApplication.run(Main.class, args);
        final var quizService = context.getBean(QuizService.class);
        quizService.startTesting();
    }
}
