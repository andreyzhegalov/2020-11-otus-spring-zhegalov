package ru.otus.spring.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ru.otus.spring.hw.service.IOService;
import ru.otus.spring.hw.service.IOServiceConsole;
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
    IOService ioController() {
        return new IOServiceConsole(System.out, System.in);
    }
}
