package ru.otus.spring.hw.shell;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.config.AppProps;
import ru.otus.spring.hw.service.JobService;

@RequiredArgsConstructor
@ShellComponent
public class ApplicationCommands {
    private static final String JOB_NAME = "migrationJob";

    private final JobLauncher jobLauncher;
    private final Job migrationJob;
    private final AppProps appProps;
    private final MongoTemplate mongoTemplate;
    private final JobService jobService;

    @ShellMethod(value = "startMigration", key = { "sm", "start-migration" })
    public void startMigration(@ShellOption String message) throws Exception {
        final var parameters = new JobParametersBuilder().addString("message", message).toJobParameters();
        jobService.allowStartIfComplete(JOB_NAME, parameters);
        final var execution = jobLauncher.run(migrationJob, parameters);
        System.out.println(execution);
    }

    @ShellMethod(value = "printCollection", key = { "pc", "print-collection" })
    public void printCollection() throws Exception {
        mongoTemplate.getCollection(appProps.getCollectionName()).find().forEach(System.out::println);
    }

    @ShellMethod(value = "dropCollection", key = { "dc", "drop-collection" })
    public void dropCollection() {
        mongoTemplate.dropCollection(appProps.getCollectionName());
    }
}
