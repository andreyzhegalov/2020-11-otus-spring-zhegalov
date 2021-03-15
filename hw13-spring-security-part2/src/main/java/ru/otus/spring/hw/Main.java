package ru.otus.spring.hw;

import com.github.cloudyrock.spring.v5.EnableMongock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableMongock
@SpringBootApplication
@EnableConfigurationProperties
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
