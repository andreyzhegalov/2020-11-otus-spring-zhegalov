package ru.otus.spring.hw.service;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JobService {
    private final JobRepository jobRepository;

    public void allowStartIfComplete(String jobName, JobParameters parameters) {
        final var repositoryJobExecution = jobRepository.getLastJobExecution(jobName, parameters);
        if (repositoryJobExecution == null) {
            return;
        }

        repositoryJobExecution.setStatus(BatchStatus.STOPPED);
        jobRepository.update(repositoryJobExecution);
    }
}
