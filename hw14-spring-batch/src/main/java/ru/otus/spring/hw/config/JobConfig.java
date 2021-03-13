package ru.otus.spring.hw.config;


import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import ru.otus.spring.hw.config.mappers.BookMapper;
import ru.otus.spring.hw.model.Book;

@Configuration
@EnableBatchProcessing
public class JobConfig {

    @StepScope
    @Bean
    public JdbcCursorItemReader<Book> bookReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Book>()
            .name("bookItemReader")
            .dataSource(dataSource).sql("SELECT id, title FROM books")
            .rowMapper(new BookMapper())
            .build();
    }

    @StepScope
    @Bean
    public MongoItemWriter<Book> writer(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<Book>().collection("books")
            .template(mongoTemplate)
            .build();
    }


    @Bean
    public Job makeJob(JobBuilderFactory jobBuilderFactory, Step step1) {
        return jobBuilderFactory.get("Test")
            .start(step1).build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Book> bookReader, ItemWriter bookWriter) {
        return stepBuilderFactory.get("step1")
            .chunk(2)
            .reader(bookReader)
            .writer(bookWriter)
            .build();
    }

}
