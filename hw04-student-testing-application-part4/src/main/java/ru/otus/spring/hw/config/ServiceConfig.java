package ru.otus.spring.hw.config;

import java.util.Scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.otus.spring.hw.dao.CsvQuestionDao;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.service.IOService;
import ru.otus.spring.hw.service.IOServiceConsole;
import ru.otus.spring.hw.io.ScannerInputStream;
import ru.otus.spring.hw.io.ScannerInputStreamImpl;

@Configuration
public class ServiceConfig {

    @Bean
    QuestionDao questionDao(DataSourceConfig dataConfig) {
        return new CsvQuestionDao(dataConfig.getFilename());
    }

    @Bean
    ScannerInputStream scannerInputStream() {
        return new ScannerInputStreamImpl(new Scanner(System.in));
    }

    @Bean
    IOService ioController(ScannerInputStream scannerInputStream) {
        return new IOServiceConsole(System.out, scannerInputStream);
    }
}
