package ru.otus.spring.hw.config;


import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;

import ru.otus.spring.hw.model.Book;

@Configuration
@EnableBatchProcessing
public class JobConfig {

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            final long bookId = rs.getLong("id");
            final String title = rs.getNString("title");
            final var book = new Book();
            book.setId( bookId );
            book.setTitle(title);
            return book;
        }
    }

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
    public FlatFileItemWriter<Book> writer() {
        return new FlatFileItemWriterBuilder<Book>().name("bookItemWriter")
                .resource(new FileSystemResource("books.txt")).lineAggregator(new DelimitedLineAggregator<>())
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
