package ru.otus.spring.hw.config;

import java.util.ArrayList;

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
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import ru.otus.spring.hw.converters.BookConverter;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.service.BookService;

@Configuration
@EnableBatchProcessing
public class JobConfig {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private AppProps appProps;


    @Bean
    public CompositeItemProcessor<Book<Long>, Book<ObjectId>> compositeProcessor(BookService bookService) {
        final var compositeProcessor = new CompositeItemProcessor<Book<Long>, Book<ObjectId>>();
        final var itemProcessors = new ArrayList<ItemProcessor<?, ?>>();
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
            @Qualifier("dropCollectionStep") Step dropCollectionStep) {
        return jobBuilderFactory.get("migrationJob").start(dropCollectionStep).next(migrationBookStep).build();
    }

    @Bean
    public Step migrationBookStep(ItemReader<Book<Long>> bookReader, ItemWriter<Book<ObjectId>> bookWriter,
            CompositeItemProcessor<Book<Long>, Book<ObjectId>> compositeProcessor) {
        return stepBuilderFactory.get("migrationBookStep").<Book<Long>, Book<ObjectId>>chunk(8).reader(bookReader)
                .processor(compositeProcessor).writer(bookWriter).allowStartIfComplete(true).build();
    }

    @Bean
    public Step dropCollectionStep(MongoTemplate mongoTemplate) {
        return stepBuilderFactory.get("dropCollectionStep").tasklet((stepContribution, chunkContext) -> {
            mongoTemplate.dropCollection(appProps.getCollectionName());
            return RepeatStatus.FINISHED;
        }).allowStartIfComplete(true).build();
    }
}
