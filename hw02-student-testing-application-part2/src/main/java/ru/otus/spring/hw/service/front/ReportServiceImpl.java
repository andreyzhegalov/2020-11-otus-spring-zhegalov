package ru.otus.spring.hw.service.front;

import ru.otus.spring.hw.service.IOService;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.domain.Report;

@Service
public class ReportServiceImpl implements ReportService {
    private final IOService ioService;

    public ReportServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void printResult(Report report) {
        ioService.print(report.print());
    }

}
