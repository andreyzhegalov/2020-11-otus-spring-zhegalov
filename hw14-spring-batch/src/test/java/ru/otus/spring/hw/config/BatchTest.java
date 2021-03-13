package ru.otus.spring.hw.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

@SpringBootTest
@SpringBatchTest
public class BatchTest {
    private final static String BOOK_COLLECTION_NAME = "books";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcCursorItemReader<Book> itemReader;

    @Autowired
    private MongoItemWriter<Book> itemWriter;

    @Autowired
    private MongoTemplate mongoTemplate;

    private JobParameters defaultJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        return paramsBuilder.toJobParameters();
    }

    @BeforeEach
    void setUp(){
        itemWriter.setCollection(BOOK_COLLECTION_NAME);
    }

    @AfterEach
    void tearDown(){
        mongoTemplate.dropCollection(BOOK_COLLECTION_NAME);
    }

    @Test
    void bookItemReaderShouldReadAllBookFromDataBase() throws Exception  {
        final var stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());
        final var bookList = new ArrayList<Book>();

        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            Book book;
            itemReader.open(stepExecution.getExecutionContext());
            while ((book = itemReader.read()) != null) {
                bookList.add(book);
            }
            itemReader.close();
            return null;
        });

        assertThat(bookList).hasSize(2);
        final var genre = new Genre();
        genre.setId(2L);
        genre.setName("genre2");
        assertThat(bookList.get(0)).isEqualTo(new Book(1, "book1", genre));
    }

    @Test
    void theBookMustBeWrittenInMongo() throws Exception{
        assertThat(mongoTemplate.findAll(Book.class, BOOK_COLLECTION_NAME)).isEmpty();

        final var stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            itemWriter.write(Collections.singletonList(new Book(1L, "book1", null)));
            return null;
        });

        final var bookCollectionName =  mongoTemplate.findAll(Book.class, BOOK_COLLECTION_NAME);
        assertThat(bookCollectionName).hasSize(1);
    }

    @Test
    void givenDbWithBook_whenReadBookFromDb_thenBookShouldBeWrittenInMongo() throws Exception {
        final var jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution).isNotNull();
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}
