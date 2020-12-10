package ru.otus.spring.hw.service.front;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.service.IOLocalizedService;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final static int THRESHOLD = 50;
    private final static String SUCCESSFUL_REPORT_KEY = "report.success";
    private final static String FAIL_REPORT_KEY = "report.fail";

    private final IOLocalizedService ioLocalizesService;

    @Override
    public void printResult(Report report) {
        final int totalCount = report.getResult().size();
        final int correctAnswerCount = getCorrectAnswerCount(report);
        final boolean isSuccess = isSuccess(totalCount, correctAnswerCount);
        final var templateKey = isSuccess ? SUCCESSFUL_REPORT_KEY : FAIL_REPORT_KEY;
        ioLocalizesService.print(templateKey, report.getStudent().getName(), report.getStudent().getSecondName(),
                correctAnswerCount, totalCount);
    }

    private int getCorrectAnswerCount(Report report) {
        return (int) report.getResult().stream().filter(p -> p.getQuestion().getAnswer().equals(p.getAnswer())).count();
    }

    private boolean isSuccess(int totalCount, int correctCount) {
        boolean isSuccess = false;
        if (totalCount != 0) {
            isSuccess = ((double) correctCount / totalCount * 100) > THRESHOLD;
        }
        return isSuccess;
    }
}
