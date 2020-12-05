package ru.otus.spring.hw.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix="dataset")
public class DataSourceConfig {
    private String filename;
}

