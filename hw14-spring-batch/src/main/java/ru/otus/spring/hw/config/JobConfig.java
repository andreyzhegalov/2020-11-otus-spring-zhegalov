package ru.otus.spring.hw.config;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.bson.types.ObjectId;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import ru.otus.spring.hw.converters.BookConverter;
import ru.otus.spring.hw.dao.mappers.BookMapper;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.service.BookService;

@Configuration
@EnableBatchProcessing
public class JobConfig {
    private static final String SQL_SELECT_ALL_BOOKS = "select books.id as book_id, title, genres.id as genre_id, genres.name as genre_name from "
            + " books left join genres on books.genre_id=genres.id";
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private AppProps appProps;

    @Bean
    public JdbcCursorItemReader<Book<Long>> bookReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Book<Long>>().name("bookItemReader").dataSource(dataSource)
                .sql(SQL_SELECT_ALL_BOOKS).rowMapper(new BookMapper()).build();
    }

    @Bean
    public CompositeItemProcessor<Book<Long>, Book<ObjectId>> compositeProcessor(BookService bookService) {
        CompositeItemProcessor<Book<Long>, Book<ObjectId>> compositeProcessor = new CompositeItemProcessor<Book<Long>, Book<ObjectId>>();
        final var itemProcessors = new ArrayList<ItemProcessor<?, ?>>();
        itemProcessors.add((ItemProcessor<Book<Long>, Book<Long>>) bookService::addAuthors);
        itemProcessors.add((ItemProcessor<Book<Long>, Book<ObjectId>>) BookConverter::convertId);
        compositeProcessor.setDelegates(itemProcessors);
        return compositeProcessor;
    }

    @Bean
    public MongoItemWriter<Book<ObjectId>> writer(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<Book<ObjectId>>().collection(appProps.getCollectionName())
                .template(mongoTemplate).build();
    }

    @Bean
    public Job makeMigrationJob(@Qualifier("migrationBookStep") Step migrationBookStep,
            @Qualifier("dropCollection") Step dropCollection) {
        return jobBuilderFactory.get("migrationJob").start(dropCollection).next(migrationBookStep).build();
    }

    @Bean
    public Step migrationBookStep(ItemReader<Book<Long>> bookReader, ItemWriter<Book<ObjectId>> bookWriter,
            CompositeItemProcessor<Book<Long>, Book<ObjectId>> compositeProcessor) {
        return stepBuilderFactory.get("migrationBookStep").<Book<Long>, Book<ObjectId>>chunk(8).reader(bookReader)
                .processor(compositeProcessor).writer(bookWriter).allowStartIfComplete(true).build();
    }

    @Bean
    public Step dropCollection(MongoTemplate mongoTemplate) {
        return stepBuilderFactory.get("dropCollection").tasklet((stepContribution, chunkContext) -> {
            mongoTemplate.dropCollection(appProps.getCollectionName());
            return RepeatStatus.FINISHED;
        }).allowStartIfComplete(true).build();
    }
}
