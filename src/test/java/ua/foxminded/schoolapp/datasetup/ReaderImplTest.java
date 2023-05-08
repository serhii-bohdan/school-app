package ua.foxminded.schoolapp.datasetup;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.exception.*;

class ReaderImplTest {

    static final String TEST_FILE = "test.txt";

    Reader reader;

    @BeforeEach
    void setUp() {
        reader = new ReaderImpl();
    }

    @Test
    void readFileAndPopulateList_shouldReadedLinesFromFile_whenFileIsCorrect() throws Exception {
        String testFileContent = """
                Line_1
                Line_2
                Line_3""";

        List<String> expectedFileLinesList = new ArrayList<>();
        expectedFileLinesList.add("Line_1");
        expectedFileLinesList.add("Line_2");
        expectedFileLinesList.add("Line_3");

        File testFile = new File("src/test/resources/" + TEST_FILE);
        FileWriter writer = new FileWriter(testFile);
        writer.write(testFileContent);
        writer.close();

        List<String> actualFileLinesList = reader.readFileAndPopulateList(TEST_FILE);
        File file = new File("src/test/resources/" + TEST_FILE);
        file.delete();

        assertEquals(expectedFileLinesList, actualFileLinesList);
    }


    @Test
    void readFileAndPopulateList_shouldFileReadingException_whenFilePathIsIncorrect() {

        assertThrows(FileReadingException.class, () -> {
            reader.readFileAndPopulateList("incorrect.txt");
        });
    }

}
