package ru.otus.spring.hw.service.front;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.service.IOLocalizedService;
import ru.otus.spring.hw.service.IOService;
import ru.otus.spring.hw.service.LocalizationService;

@SpringBootTest
public class UserServiceImplTest {

    @MockBean
    private IOService ioService;

    @MockBean
    private LocalizationService localizationService;

    @Autowired
    private IOLocalizedService ioLocalizeService;

    @Test
    void getStudentName() {
        given(ioService.read()).willReturn("name").willReturn("second name");

        final var student = new UserServiceImpl(ioLocalizeService).getStudent();

        InOrder inOrder = Mockito.inOrder(localizationService);
        then(localizationService).should(inOrder).getText("get.user.name");
        then(localizationService).should(inOrder).getText("get.user.second.name");

        assertThat(student.getName()).isEqualTo("name");
        assertThat(student.getSecondName()).isEqualTo("second name");
    }
}
