package ru.otus.spring.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.controller.IOControllerConsole;
import ru.otus.spring.hw.dao.CsvQuestionDao;
import ru.otus.spring.hw.dao.QuestionDao;

@Configuration
public class ServiceConfig {

    @Bean
    QuestionDao questionDao(DataSourceConfig dataConfig) {
        return new CsvQuestionDao(dataConfig.getFilename());
    }

    @Bean
    IOController ioController() {
        return new IOControllerConsole(System.out, System.in);
    }
}
