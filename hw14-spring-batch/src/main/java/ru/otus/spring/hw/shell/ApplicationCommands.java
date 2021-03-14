package ru.otus.spring.hw.shell;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.config.AppProps;

@RequiredArgsConstructor
@ShellComponent
public class ApplicationCommands {

    private final JobLauncher jobLauncher;
    private final Job migrationJob;
    private final AppProps appProps;
    private final MongoTemplate mongoTemplate;

    @ShellMethod(value = "startMigration", key = { "sm", "start-migration" })
    public void startMigration() throws Exception {
        final var execution = jobLauncher.run(migrationJob, new JobParametersBuilder().toJobParameters());
        System.out.println(execution);
    }

    @ShellMethod(value = "printCollection", key = { "pc", "print-collection" })
    public void printCollection() throws Exception {
        mongoTemplate.getCollection(appProps.getCollectionName()).find().forEach(System.out::println);
    }

    @ShellMethod(value = "dropCollection", key = {"dc", "drop-collection"})
    public void dropCollection(){
        mongoTemplate.dropCollection(appProps.getCollectionName());
    }
}
