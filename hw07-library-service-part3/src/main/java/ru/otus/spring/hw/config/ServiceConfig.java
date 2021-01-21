package ru.otus.spring.hw.config;

import java.util.Scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.otus.spring.hw.io.ScannerInputStream;
import ru.otus.spring.hw.io.ScannerInputStreamImpl;
import ru.otus.spring.hw.service.IOService;
import ru.otus.spring.hw.service.IOServiceConsole;

@Configuration
public class ServiceConfig {

    @Bean
    ScannerInputStream scannerInputStream() {
        return new ScannerInputStreamImpl(new Scanner(System.in));
    }

    @Bean
    IOService ioController(ScannerInputStream scannerInputStream) {
        return new IOServiceConsole(System.out, scannerInputStream);
    }
}
