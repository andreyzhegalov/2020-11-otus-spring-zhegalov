package ru.otus.spring.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.controller.IOControllerConsole;
import ru.otus.spring.hw.dao.CsvQuestionDao;
import ru.otus.spring.hw.dao.QuestionDao;

@PropertySource("classpath:application.properties")
@Configuration
public class ServiceConfig {

    @Value("${dataset.filename}")
    private String datasetFile;

    @Bean
    QuestionDao questionDao() {
        return new CsvQuestionDao(datasetFile);
    }

    @Bean
    IOController ioController() {
        return new IOControllerConsole(System.out, System.in);
    }
}
