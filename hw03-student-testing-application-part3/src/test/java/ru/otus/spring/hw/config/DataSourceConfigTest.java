package ru.otus.spring.hw.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DataSourceConfigTest {

    @ParameterizedTest
    @CsvSource({ "en, folder/test_en.csv", "ru-RU, folder/test_ru_RU.csv" })
    void fileNameShouldContainLocale(String in, String expected) {
        final var fileName = "folder/test.csv";
        final var locale = Locale.forLanguageTag(in);
        final var props = new AppProps(locale);

        assertThat(new DataSourceConfig(fileName, props).getFilename()).isEqualTo(expected);
    }
}
