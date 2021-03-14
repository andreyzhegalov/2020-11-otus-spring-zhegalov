package ru.otus.spring.hw.config;

import java.util.ArrayList;

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
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import ru.otus.spring.hw.dao.mappers.BookMapper;
import ru.otus.spring.hw.dto.BookDb;
import ru.otus.spring.hw.dto.BookMongo;
import ru.otus.spring.hw.service.BookService;

@Configuration
@EnableBatchProcessing
public class JobConfig {
    private final static String BOOK_TABLE_NAME = "books";
    private final static String BOOK_COLLECTION_NAME = "books";

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @StepScope
    @Bean
    public JdbcCursorItemReader<BookDb> bookReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<BookDb>().name("bookItemReader").dataSource(dataSource)
                .sql("select books.id as book_id, title, genres.id as genre_id, genres.name as genre_name from "
                        + BOOK_TABLE_NAME + " left join genres on books.genre_id=genres.id")
                .rowMapper(new BookMapper()).build();
    }

    @StepScope
    @Bean
    public CompositeItemProcessor<BookDb, BookMongo> compositeProcessor(BookService bookService) {
        CompositeItemProcessor<BookDb, BookMongo> compositeProcessor = new CompositeItemProcessor<BookDb, BookMongo>();
        final var itemProcessors = new ArrayList<ItemProcessor<?, ?>>();
        itemProcessors.add((ItemProcessor<BookDb, BookDb>) bookService::addAuthors);
        itemProcessors.add((ItemProcessor<BookDb, BookMongo>) BookMongo::new );
        compositeProcessor.setDelegates(itemProcessors);
        return compositeProcessor;
    }

    @StepScope
    @Bean
    public MongoItemWriter<BookMongo> writer(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<BookMongo>().collection(BOOK_COLLECTION_NAME).template(mongoTemplate).build();
    }

    @Bean
    public Job makeMigrationJob(JobBuilderFactory jobBuilderFactory, Step migrationBookStep) {
        return jobBuilderFactory.get("migrationJob").start(migrationBookStep).build();
    }

    @Bean
    public Step migrationBookStep(ItemReader<BookDb> bookReader, ItemWriter<BookMongo> bookWriter,
            CompositeItemProcessor<BookDb, BookMongo> compositeProcessor) {
        return stepBuilderFactory.get("migrationBookStep").<BookDb, BookMongo>chunk(8).reader(bookReader)
                .processor(compositeProcessor).writer(bookWriter).build();
    }

}
