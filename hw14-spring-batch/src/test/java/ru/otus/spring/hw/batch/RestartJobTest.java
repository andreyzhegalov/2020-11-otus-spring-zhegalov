package ru.otus.spring.hw.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBatchTest
public class RestartJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Test
    void shouldThrowExceptionIfExecutionJobIstanceCompleted() throws Exception {
        final var parameters = new JobParametersBuilder().addString("parameter1", "value").toJobParameters();

        final var jobExecution = jobLauncher.launchJob(parameters);
        assertThat(jobExecution.getStepExecutions()).hasSize(2);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        assertThatCode(() -> jobLauncher.launchJob(parameters)).isInstanceOf(JobInstanceAlreadyCompleteException.class);
    }

    private void resetJob(String jobName, JobParameters parameters) {
        final var repositoryJobExecution = jobRepository.getLastJobExecution(jobName, parameters);

        repositoryJobExecution.setStatus(BatchStatus.STOPPED);
        jobRepository.update(repositoryJobExecution);

        repositoryJobExecution.getStepExecutions().forEach(step -> {
            step.setStatus(BatchStatus.STOPPED);
            jobRepository.update(step);
        });
    }

    @Test
    void shouldRestartJobWithSameParameters() throws Exception {
        final var parameters = new JobParametersBuilder().addString("parameter1", "value").toJobParameters();

        final var jobExecution = jobLauncher.launchJob(parameters);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(jobExecution.getStepExecutions()).hasSize(2);
        final var jobName = jobExecution.getJobInstance().getJobName();

        resetJob(jobName, parameters);

        final var restartJobExecution = jobLauncher.launchJob(parameters);
        assertThat(restartJobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(restartJobExecution.getStepExecutions()).hasSize(2);
    }
}
