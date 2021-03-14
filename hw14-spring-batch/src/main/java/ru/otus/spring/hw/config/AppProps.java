package ru.otus.spring.hw.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(value = "app")
public class AppProps {
    private String collectionName;
}
