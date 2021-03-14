package ru.otus.spring.hw.shell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;

@SpringBootTest
class ApplicationCommandsTest {
    @Autowired
    private Shell shell;

    @MockBean
    private JobLauncher jobLauncher;

    @Test
    void printShouldPrintAllBooks() throws Exception {
        shell.evaluate(() -> "start-migration param");
        then(jobLauncher).should().run(any(), any());
    }
}
