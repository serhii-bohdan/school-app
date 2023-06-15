package ua.foxminded.schoolapp.datasetup.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolapp.datasetup.Reader;
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
        StringBuilder content = new StringBuilder();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePathInResources);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (Exception e) {
            throw new FileReadingException("Failed to read file " + filePathInResources + ". Make sure this file exists.");
        }
        return content.toString().strip();
    }

}
