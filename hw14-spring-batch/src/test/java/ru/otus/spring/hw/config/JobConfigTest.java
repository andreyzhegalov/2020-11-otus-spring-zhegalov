package ru.otus.spring.hw.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import ru.otus.spring.hw.model.Book;

@AutoConfigureDataMongo
@SpringBootTest
@SpringBatchTest
public class JobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MongoItemWriter<Book<ObjectId>> itemWriter;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AppProps appProps;

    private JobParameters defaultJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        return paramsBuilder.toJobParameters();
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(appProps.getCollectionName());
    }

    @Test
    @DirtiesContext
    void theBookMustBeWrittenInMongo() throws Exception {
        assertThat(mongoTemplate.findAll(Book.class, appProps.getCollectionName())).isEmpty();
        final var book = new Book<ObjectId>();
        book.setId(new ObjectId());
        book.setTitle("book1");

        final var stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            itemWriter.write(Collections.singletonList(book));
            return null;
        });

        assertThat(mongoTemplate.findAll(Book.class, appProps.getCollectionName())).hasSize(1);
    }

    @Test
    @DirtiesContext
    void stepTaskletDropCollectionCompleteSuccessfully() throws Exception {
        final var book = new Book<ObjectId>();
        book.setId(new ObjectId());
        mongoTemplate.save(book, appProps.getCollectionName());
        assertThat(mongoTemplate.findAll(Book.class, appProps.getCollectionName())).isNotEmpty();

        final var jobExecution = jobLauncherTestUtils.launchStep("dropCollectionStep");
        final var actualStepExecutions = jobExecution.getStepExecutions();
        final var actualExitStatus = jobExecution.getExitStatus();

        assertThat(actualStepExecutions).hasSize(1);
        assertThat(actualExitStatus.getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
        assertThat(mongoTemplate.findAll(Book.class, appProps.getCollectionName())).isEmpty();
    }

    @Test
    @DirtiesContext
    void stepMigrationBookShouldCompleteSuccessfully() {
        final var jobExecution = jobLauncherTestUtils.launchStep("migrationBookStep", defaultJobParameters());
        final var actualStepExecutions = jobExecution.getStepExecutions();
        final var actualExitStatus = jobExecution.getExitStatus();

        assertThat(actualStepExecutions).hasSize(1);
        assertThat(actualExitStatus.getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
        actualStepExecutions.forEach(stepExecution -> {
            assertThat(stepExecution.getReadCount()).isNotZero();
            assertThat(stepExecution.getWriteCount()).isEqualTo(stepExecution.getReadCount());
        });
    }

    @Test
    @DirtiesContext
    void migrationJobShouldCompleteSuccessfully() throws Exception {
        final var jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution).isNotNull();
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        final var savedBooks = mongoTemplate.findAll(Book.class, appProps.getCollectionName());
        assertThat(savedBooks).hasSize(2).allMatch(s -> !s.getTitle().equals(""))
                .allMatch(s -> !s.getAuthors().isEmpty()).allMatch(s -> s.getGenre() != null);
    }
}
