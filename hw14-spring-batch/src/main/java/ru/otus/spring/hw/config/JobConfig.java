package ru.otus.spring.hw.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
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
import ru.otus.spring.hw.service.BookService;

@Configuration
@EnableBatchProcessing
public class JobConfig {
    private final static String BOOK_TABLE_NAME = "books";
    private final static String BOOK_COLLECTION_NAME = "books";

    @StepScope
    @Bean
    public JdbcCursorItemReader<Book> bookReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Book>().name("bookItemReader").dataSource(dataSource)
                .sql("select books.id as book_id, title, genres.id as genre_id, genres.name as genre_name from "
                        + BOOK_TABLE_NAME + " left join genres on books.genre_id=genres.id")
                .rowMapper(new BookMapper()).build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Book, Book> processor(BookService bookService) {
        return (ItemProcessor<Book, Book>) bookService::addAuthors;
    }

    @StepScope
    @Bean
    public MongoItemWriter<Book> writer(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<Book>().collection(BOOK_COLLECTION_NAME).template(mongoTemplate).build();
    }

    @Bean
    public Job makeJob(JobBuilderFactory jobBuilderFactory, Step step1) {
        return jobBuilderFactory.get("Test").start(step1).build();
    }

    @Bean
    public Step importBookStep(StepBuilderFactory stepBuilderFactory, ItemReader<Book> bookReader,
            ItemWriter<Book> bookWriter) {
        return stepBuilderFactory.get("importBookStep").<Book, Book>chunk(8).reader(bookReader).writer(bookWriter)
                .build();
    }

}
