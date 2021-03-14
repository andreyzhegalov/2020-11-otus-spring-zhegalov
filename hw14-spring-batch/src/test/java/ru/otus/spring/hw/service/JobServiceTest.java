package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

@SpringBootTest
@SpringBatchTest
public class JobServiceTest {

    @Autowired
    private JobLauncherTestUtils jobLauncher;

    @Autowired
    private JobService jobService;

    // @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    // @Test
    // void shouldThrowExceptionIfExecutionJobIstanceCompleted() throws Exception {
    //     final var parameters = new JobParametersBuilder().addString("parameter1", "value").toJobParameters();
    //
    //     final var jobExecution = jobLauncher.launchJob(parameters);
    //     assertThat(jobExecution.getStepExecutions()).hasSize(2);
    //     assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    //
    //     assertThatCode(() -> jobLauncher.launchJob(parameters)).isInstanceOf(JobInstanceAlreadyCompleteException.class);
    // }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    void shouldRestartJobWithSameParameters() throws Exception {
        final var parameters = new JobParametersBuilder().addString("parameter1", "value").toJobParameters();

        final var jobExecution = jobLauncher.launchJob(parameters);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(jobExecution.getStepExecutions()).hasSize(2);
        final var jobName = jobExecution.getJobInstance().getJobName();

        jobService.resetJob(jobName, parameters);

        final var restartJobExecution = jobLauncher.launchJob(parameters);
        assertThat(restartJobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(restartJobExecution.getStepExecutions()).hasSize(2);
    }
}
