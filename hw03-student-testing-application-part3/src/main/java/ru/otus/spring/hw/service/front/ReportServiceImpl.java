package ru.otus.spring.hw.service.front;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ru.otus.spring.hw.config.AppProps;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.service.IOService;

@Service
public class ReportServiceImpl implements ReportService {
    private final static int THRESHOLD = 50;
    private final static String SUCCESSFUL_REPORT_KEY = "report.success";
    private final static String FAIL_REPORT_KEY = "report.fail";

    private final IOService ioService;
    private final MessageSource messageSource;
    private final AppProps props;

    public ReportServiceImpl(IOService ioService, MessageSource messageSource, AppProps props) {
        this.ioService = ioService;
        this.messageSource = messageSource;
        this.props = props;
    }

    @Override
    public void printResult(Report report) {
        final int totalCount = report.getResult().size();
        final int correctAnswerCount = getCorrectAnswerCount(report);
        final boolean isSuccess = isSuccess(totalCount, correctAnswerCount);

        final var templateKey = isSuccess ? SUCCESSFUL_REPORT_KEY : FAIL_REPORT_KEY;
        final var message = messageSource.getMessage(templateKey, new Object[] { report.getStudent().getName(),
                report.getStudent().getSecondName(), correctAnswerCount, totalCount }, props.getLocale());

        ioService.print(message);
    }

    private int getCorrectAnswerCount(Report report) {
        return (int)report.getResult().stream().filter(p -> p.getQuestion().getAnswer().equals(p.getAnswer())).count();
    }

    private boolean isSuccess(int totalCount, int correctCount) {
        boolean isSuccess = false;
        if (totalCount != 0) {
            isSuccess = ((double) correctCount / totalCount * 100) > THRESHOLD;
        }
        return isSuccess;
    }
}
