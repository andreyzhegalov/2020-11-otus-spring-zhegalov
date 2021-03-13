package ru.otus.spring.hw.shell;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ShellComponent
public class ApplicationCommands {

    private final JobLauncher jobLauncher;
    private final Job migrationJob;

    @ShellMethod(value = "startMigration", key = { "sm", "start-migration" })
    public void startMigration() throws Exception {
        final var execution = jobLauncher.run(migrationJob, new JobParametersBuilder().toJobParameters());
        System.out.println(execution);
    }
}
