package ua.foxminded.schoolapp;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.cli.SchoolController;

@SpringBootTest(classes = { Main.class })
class MainTest {

    @MockBean
    SchoolController controllerMock;

    @Autowired
    Main mainMock;

    @Test
    void run_shouldInvokedAllMethodsInMainMethod_whenTheseMethodsWorkCorrectly() {
        mainMock.run();

        verify(controllerMock, times(1)).runSchoolApp();
    }

}
