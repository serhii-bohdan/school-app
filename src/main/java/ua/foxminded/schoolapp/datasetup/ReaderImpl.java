package ua.foxminded.schoolapp.datasetup;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import ua.foxminded.schoolapp.exception.FileReadingException;

public class ReaderImpl implements Reader {

    public List<String> readFileAndPopulateList(String filePathInResources) {
        List<String> lines = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePathInResources);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            lines = reader.lines()
                          .map(String::strip)
                          .filter(stripedLine -> !stripedLine.isEmpty())
                          .toList();
        } catch (Exception e) {
            throw new FileReadingException("Failed to read file: " + filePathInResources + ". Make sure this file exists.");
        }
        return lines;
    }

    public String readAllFileToString(String filePathInResources) {
        String script = null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File scriptFile = new File(classLoader.getResource(filePathInResources).getFile());
            script = FileUtils.readFileToString(scriptFile, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new FileReadingException("Failed to read file " + filePathInResources + ". Make sure this file exists.");
        }
        return script.strip();
    }

}
