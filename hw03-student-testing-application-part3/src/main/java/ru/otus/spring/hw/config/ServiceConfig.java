package ru.otus.spring.hw.config;

import java.util.Locale;

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
        final var fileNameWithLocale = getFileNameWithLocale(dataConfig.getFilename(), appProps.getLocale());
        return new CsvQuestionDao(fileNameWithLocale);
    }

    private String getFileNameWithLocale(String initFileName, Locale locale) {
        final var fileName = initFileName.substring(0, initFileName.lastIndexOf("."));
        final var fileExtension = initFileName.substring(initFileName.lastIndexOf("."), initFileName.length());
        final var localeSuffix = locale.toString();
        return String.format("%s_%s%s", fileName, localeSuffix, fileExtension);
    }

    @Bean
    IOService ioController() {
        return new IOServiceConsole(System.out, System.in);
    }
}
