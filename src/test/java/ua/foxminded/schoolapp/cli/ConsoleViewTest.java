package ua.foxminded.schoolapp.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsoleViewTest {

    View view;
    Scanner scannerMock;
    ByteArrayOutputStream baos;
    PrintStream printStreamMock;

    @BeforeEach
    void setUp() throws Exception {
        scannerMock = mock(Scanner.class);
        baos = new ByteArrayOutputStream();
        printStreamMock = new PrintStream(baos);
        System.setOut(printStreamMock);
    }

    @Test
    void consoleView_shouldNullPointerException_whenScannerIsNull() {
        assertThrows(NullPointerException.class, () -> new ConsoleView(null));
    }

    @Test
    void showMenu_shouldDisplayedProgramMenu_whenInvokeShowMenu() {
        view = new ConsoleView(scannerMock);
        String expectedOutput = """
                            **************************
                            -----   SCHOOL APP   -----
                            **************************
                1. Find all groups with less or equal students’ number.
                2. Find all students related to the course with the given name.
                3. Add a new student.
                4. Delete a student.
                5. Add a student to the course.
                6. Remove the student from one of their courses.

                Enter 0 to exit the program.
                \r\n""";

        view.showMenu();
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedNullWord_whenMessageIsNull() {
        view = new ConsoleView(scannerMock);
        String expectedOutput = "null\r\n";

        view.printMessage(null);
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedOnlyCRLF_whenMessageIsEmptyString() {
        view = new ConsoleView(scannerMock);
        String expectedOutput = "\r\n";

        view.printMessage("");
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedWord_whenMessageIsOnlyOneWord() {
        view = new ConsoleView(scannerMock);
        String expectedOutput = "Word\r\n";

        view.printMessage("Word");
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void getIntNumberFromUser_shouldReturnedIntegerNumberOnFirstAttempt_whenScannerMockReturnIntegerInput() {
        view = new ConsoleView(scannerMock);
        int expectedIntFromUser = 4;
        when(scannerMock.hasNextInt()).thenReturn(true);
        when(scannerMock.nextInt()).thenReturn(expectedIntFromUser);

        int actualIntFromUser = view.getIntNumberFromUser("Message");

        verify(scannerMock, times(1)).hasNextInt();
        verify(scannerMock, times(1)).nextInt();
        assertEquals(expectedIntFromUser, actualIntFromUser);
    }

    @Test
    void getIntNumberFromUser_shouldReturnedIntegerNumberOnSecondAttempt_whenScannerMockReturnIncorrectValueOnFirstAttempt() {
        view = new ConsoleView(scannerMock);
        int expectedIntFromUser = 3;
        when(scannerMock.hasNextInt()).thenReturn(false, true);
        when(scannerMock.next()).thenReturn("Not integer");
        when(scannerMock.nextInt()).thenReturn(expectedIntFromUser);

        int actualIntFromUser = view.getIntNumberFromUser("Message");

        verify(scannerMock, times(2)).hasNextInt();
        verify(scannerMock, times(1)).next();
        verify(scannerMock, times(1)).nextInt();
        assertEquals(expectedIntFromUser, actualIntFromUser);
    }

    @Test
    void getWordFromUser_shouldReturnedEmptyString_whenScannerMockReturEmptyString() {
        view = new ConsoleView(scannerMock);
        String expectedReturnedWord = "";
        when(scannerMock.next()).thenReturn(expectedReturnedWord);

        String actualReturnedWord = view.getWordFromUser("Message");

        assertEquals(expectedReturnedWord, actualReturnedWord);
    }

    @Test
    void getWordFromUser_shouldReturnedOnlySpaces_whenScannerMockReturnOnlySpaces() {
        view = new ConsoleView(scannerMock);
        String expectedReturnedWord = "          ";
        when(scannerMock.next()).thenReturn(expectedReturnedWord);

        String actualReturnedWord = view.getWordFromUser("Message");

        assertEquals(expectedReturnedWord, actualReturnedWord);
    }

    @Test
    void getWordFromUser_shouldReturnedSimpleWord_whenScannerMockReturnSimpleWord() {
        view = new ConsoleView(scannerMock);
        String expectedReturnedWord = "Art";
        when(scannerMock.next()).thenReturn(expectedReturnedWord);

        String actualReturnedWord = view.getWordFromUser("Message");

        assertEquals(expectedReturnedWord, actualReturnedWord);
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

}
