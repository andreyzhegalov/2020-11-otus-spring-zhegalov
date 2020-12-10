package ru.otus.spring.hw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Setter;

@Setter
@ConfigurationProperties(prefix = "dataset")
public class DataSourceConfig {
    private String filename;

    @Autowired
    private AppProps props;

    public String getFilename() {
        final var fileName = filename.substring(0, filename.lastIndexOf("."));
        final var fileExtension = filename.substring(filename.lastIndexOf("."));
        final var localeSuffix = props.getLocale().toString();
        return String.format("%s_%s%s", fileName, localeSuffix, fileExtension);
    }
}
