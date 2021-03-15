package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBatchTest
public class JobServiceTest {

    @Autowired
    private JobLauncherTestUtils jobLauncher;

    @Autowired
    private JobService jobService;

    @Test
    void restartingJobWithSameParametersShouldThrowException() throws Exception {
        final var parameters = new JobParametersBuilder().addString("parameter2", "value").toJobParameters();

        final var jobExecution = jobLauncher.launchJob(parameters);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(jobExecution.getStepExecutions()).hasSize(2);

        assertThatCode(() -> jobLauncher.launchJob(parameters)).isInstanceOf(JobInstanceAlreadyCompleteException.class);

    }

    @Test
    void shouldDoNothingForNotExistedJobName() {
        final var jobRepository = Mockito.mock(JobRepository.class);
        final var jobService = new JobService(jobRepository);
        final var parameters = new JobParametersBuilder().toJobParameters();
        given(jobRepository.getLastJobExecution("notExistedJob", parameters)).willReturn(null);
        jobService.allowStartJobIfComplete("notExistedJob", parameters);
        then(jobRepository).should(never()).update(any(JobExecution.class));
    }

    @Test
    void shouldRestartJobWithSameParameters() throws Exception {
        final var parameters = new JobParametersBuilder().addString("parameter1", "value").toJobParameters();

        final var jobExecution = jobLauncher.launchJob(parameters);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(jobExecution.getStepExecutions()).hasSize(2);
        final var jobName = jobExecution.getJobInstance().getJobName();

        jobService.allowStartJobIfComplete(jobName, parameters);

        final var restartJobExecution = jobLauncher.launchJob(parameters);
        assertThat(restartJobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(restartJobExecution.getStepExecutions()).hasSize(2);
    }
}
