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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.service.IOLocalizedService;

@SpringBootTest
public class UserServiceImplTest {

    @Import(UserServiceImpl.class)
    @Configuration
    public static class UserServiceImplTestInner {
    }

    @MockBean
    private IOLocalizedService ioLocalizedService;

    @Autowired
    private UserService userService;

    @Test
    void getStudentName() {
        given(ioLocalizedService.read()).willReturn("name").willReturn("second name");
        final var student = userService.getStudent();

        InOrder inOrder = Mockito.inOrder(ioLocalizedService);
        then(ioLocalizedService).should(inOrder).printLocalizedMessage("get.user.name");
        then(ioLocalizedService).should(inOrder).printLocalizedMessage("get.user.second.name");

        assertThat(student.getName()).isEqualTo("name");
        assertThat(student.getSecondName()).isEqualTo("second name");
    }
}
