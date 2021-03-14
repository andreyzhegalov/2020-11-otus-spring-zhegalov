package ru.otus.spring.hw.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;

import org.bson.types.ObjectId;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;

import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

@SpringBootTest
@SpringBatchTest
public class BatchTest {
    private static final String BOOK_COLLECTION_NAME = "books";
    private static final String MIGRATION_BOOK_STEP_NAME = "migrationBookStep";
    private static final int BOOK_ITEM_COUNT = 2;
	private static final String TASKLET_DROP_COLLECTION = "dropCollection";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcCursorItemReader<Book<Long>> itemReader;

    @Autowired
    private MongoItemWriter<Book<ObjectId>> itemWriter;

    @Autowired
    private MongoTemplate mongoTemplate;

    private JobParameters defaultJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        return paramsBuilder.toJobParameters();
    }

    @BeforeEach
    void setUp() {
        itemWriter.setCollection(BOOK_COLLECTION_NAME);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(BOOK_COLLECTION_NAME);
    }

    @Test
    void bookItemReaderShouldReadAllBookFromDataBase() throws Exception {
        final var stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());
        final var bookList = new ArrayList<Book<Long>>();

        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            Book<Long> book;
            itemReader.open(stepExecution.getExecutionContext());
            while ((book = itemReader.read()) != null) {
                bookList.add(book);
            }
            itemReader.close();
            return null;
        });

        final var expectedGenre = new Genre<Long>();
        expectedGenre.setId(2L);
        expectedGenre.setName("genre2");

        final var book  = bookList.get(0);
        assertThat(bookList).hasSize(2);
        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getGenre()).isEqualTo(expectedGenre);
        assertThat(book.getAuthors()).isEmpty();
    }

    @Test
    void theBookMustBeWrittenInMongo() throws Exception {
        assertThat(mongoTemplate.findAll(Book.class, BOOK_COLLECTION_NAME)).isEmpty();
        final var book = new Book<ObjectId>();
        book.setId(new ObjectId());
        book.setTitle("book1");

        final var stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            itemWriter.write(Collections.singletonList(book));
            return null;
        });

        final var bookCollectionName = mongoTemplate.findAll(Book.class, BOOK_COLLECTION_NAME);
        assertThat(bookCollectionName).hasSize(1);
    }

    @Test
    void stepTaskletDropCollectionCompleteSuccessfully() throws Exception{
        final var book = new Book<ObjectId>();
        book.setId(new ObjectId());
        book.setTitle("book1");
        mongoTemplate.save(book, BOOK_COLLECTION_NAME);
        assertThat(mongoTemplate.findAll(Book.class, BOOK_COLLECTION_NAME)).isNotEmpty();

        final var jobExecution = jobLauncherTestUtils.launchStep(TASKLET_DROP_COLLECTION);
        final var actualStepExecutions = jobExecution.getStepExecutions();
        final var actualExitStatus = jobExecution.getExitStatus();

        assertThat(actualStepExecutions).hasSize(1);
        assertThat(actualExitStatus.getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());

        assertThat(mongoTemplate.findAll(Book.class, BOOK_COLLECTION_NAME)).isEmpty();
    }

    @Test
    void stepMigrationBookShouldCompleteSuccessfully() {
        final var jobExecution = jobLauncherTestUtils.launchStep(MIGRATION_BOOK_STEP_NAME, defaultJobParameters());
        final var actualStepExecutions = jobExecution.getStepExecutions();
        final var actualExitStatus = jobExecution.getExitStatus();

        assertThat(actualStepExecutions).hasSize(1);
        assertThat(actualExitStatus.getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
        actualStepExecutions.forEach(stepExecution -> {
            assertThat(stepExecution.getWriteCount()).isEqualTo(BOOK_ITEM_COUNT);
            assertThat(stepExecution.getReadCount()).isEqualTo(BOOK_ITEM_COUNT);
        });
    }

    @Test
    void givenDbWithBook_whenReadBookFromDb_thenBookShouldBeWrittenInMongo() throws Exception {
        final var jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution).isNotNull();
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        final var savedBooks = mongoTemplate.findAll(Book.class, BOOK_COLLECTION_NAME);
        assertThat(savedBooks).hasSize(2).allMatch(s -> !s.getTitle().equals(""))
                .allMatch(s -> !s.getAuthors().isEmpty()).allMatch(s -> s.getGenre() != null);
    }

    // Job job = new SimpleJob();
    // job.setRestartable(false);
    //
    // JobParameters jobParameters = new JobParameters();
    //
    // JobExecution firstExecution = jobRepository.createJobExecution(job,
    // jobParameters);
    // jobRepository.saveOrUpdate(firstExecution);
    //
    // try {
    // jobRepository.createJobExecution(job, jobParameters);
    // fail();
    // }
    // catch (JobRestartException e) {
    // // expected
    // }

}
