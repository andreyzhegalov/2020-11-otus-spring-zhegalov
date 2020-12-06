package ru.otus.spring.hw.service.front;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.domain.Report;

public class FrontReportServiceImpl implements FrontReportService {
    private final IOController ioController;

    public FrontReportServiceImpl(IOController ioController) {
        this.ioController = ioController;
    }

    @Override
    public void printResult(Report report) {
        ioController.print(report.print());
    }

}
