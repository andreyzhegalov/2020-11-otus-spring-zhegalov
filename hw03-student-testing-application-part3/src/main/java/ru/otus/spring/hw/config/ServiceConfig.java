package ru.otus.spring.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.otus.spring.hw.dao.CsvQuestionDao;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.service.IOService;
import ru.otus.spring.hw.service.IOServiceConsole;

@Configuration
public class ServiceConfig {

    @Bean
    QuestionDao questionDao(DataSourceConfig dataConfig, AppProps appProps) {
        return new CsvQuestionDao(dataConfig.getFilename());
    }

    @Bean
    IOService ioController() {
        return new IOServiceConsole(System.out, System.in);
    }
}
