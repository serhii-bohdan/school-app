package ua.foxminded.schoolapp.datasetup.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import ua.foxminded.schoolapp.datasetup.Reader;
import ua.foxminded.schoolapp.exception.FileReadingException;

public class ReaderImpl implements Reader {

    public List<String> readFileAndPopulateList(String filePathInResources) {
        List<String> lines = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePathInResources);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            lines = reader.lines()
                          .toList();
        } catch (IOException e) {
            throw new FileReadingException("Failed to read file: " + filePathInResources);
        }
        return lines;
    }

    public String readSqlScriptFrom(String filePathInResources) {
        File scriptFile = new File("src/main/resources/" + filePathInResources);
        String script = null;

        try {
            script = FileUtils.readFileToString(scriptFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileReadingException("Failed to read file " + filePathInResources);
        }
        return script;
    }

}
