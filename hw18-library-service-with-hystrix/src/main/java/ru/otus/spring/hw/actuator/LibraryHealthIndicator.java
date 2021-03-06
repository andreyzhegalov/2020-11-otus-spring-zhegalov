package ru.otus.spring.hw.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Component
public class LibraryHealthIndicator implements HealthIndicator {
    private final BookRepository bookRepository;

    @Override
    public Health health() {
        return Health.up().withDetail("bookCnt", bookRepository.count()).build();
    }
}
