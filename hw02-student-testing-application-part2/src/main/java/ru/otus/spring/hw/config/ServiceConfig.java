package ru.otus.spring.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.controller.IOControllerConsole;
import ru.otus.spring.hw.dao.CsvQuestionDao;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.service.FrontService;
import ru.otus.spring.hw.service.FrontServiceImpl;
import ru.otus.spring.hw.service.QuizService;
import ru.otus.spring.hw.service.QuizServiceImpl;

@Configuration
public class ServiceConfig {

    @Bean
    QuizService quizService(QuestionDao questionDao, FrontService frontService) {
        return new QuizServiceImpl(questionDao, frontService);
    }

    @Bean
    QuestionDao questionDao() {
        return new CsvQuestionDao("datasets/quiz_dataset.csv");
    }

    @Bean
    FrontService frontService(IOController ioController) {
        return new FrontServiceImpl(ioController);
    }

    @Bean
    IOController ioController() {
        return new IOControllerConsole(System.out, System.in);
    }
}
