package ru.otus.spring.hw.service.front;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.service.IOService;

@Service
public class ReportServiceImpl implements ReportService {
    private final static int THRESHOLD = 50;
    private final static String REPORT_TEMPLATE_SUCCESS = "Congratulations to %s %s! You have been tested. Correct %d questions out of %d.";
    private final static String REPORT_TEMPLATE_FAIL = "%s %s, you have not been tested. Correct %d questions out of %d.";

    private final IOService ioService;

    public ReportServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void printResult(Report report) {
        final long totalCount = report.getResult().size();
        final long correctAnswerCount = report.getResult().stream()
                .filter(p -> p.getQuestion().getAnswer().equals(p.getAnswer())).count();
        boolean isSuccess = false;
        if (totalCount != 0) {
            isSuccess = ((double) correctAnswerCount / totalCount * 100) > THRESHOLD;
        }
        final var template = isSuccess ? REPORT_TEMPLATE_SUCCESS : REPORT_TEMPLATE_FAIL;
        final var text = String.format(template, report.getStudent().getName(), report.getStudent().getSecondName(),
                correctAnswerCount, totalCount);
        ioService.print(text);
    }
}
