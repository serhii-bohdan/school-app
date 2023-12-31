package ua.foxminded.schoolapp.service.generate.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolapp.exception.FileReadingException;
import ua.foxminded.schoolapp.service.generate.Reader;

/**
 * The ReaderImpl class is an implementation of the {@link Reader} interface for
 * reading file contents.
 * <p>
 * This class is annotated with {@code @Component} to indicate that it is a
 * Spring component, and it can be automatically discovered and registered as a
 * bean in the Spring context. The class uses the Java I/O classes to read the
 * content of a file from the project's resources and provides methods to return
 * the content either as a list of lines or as a single string. The lines are
 * filtered to remove leading and trailing whitespaces, and empty lines are
 * skipped.
 *
 * @author Serhii Bohdan
 */
@Component
public class ReaderImpl implements Reader {

    /**
     * The logger for logging events and messages in the {@link ReaderImpl} class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> readFileAndPopulateListWithLines(String filePathInResources) {
        LOGGER.debug("Reading file and populating list with lines: {}", filePathInResources);
        List<String> lines = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePathInResources);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            lines = reader.lines()
                    .map(String::strip)
                    .filter(stripedLine -> !stripedLine.isEmpty())
                    .toList();
        } catch (Exception e) {
            LOGGER.error("Failed to read file: {}", filePathInResources, e);
            throw new FileReadingException( "Failed to read file: " + filePathInResources
                   + ". Make sure this file exists.");
        }

        LOGGER.debug("Read {} lines from file: {}", lines.size(), filePathInResources);
        return lines;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readAllFileToString(String filePathInResources) {
        LOGGER.debug("Reading entire file to string: {}", filePathInResources);
        StringBuilder content = new StringBuilder();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePathInResources);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }

        } catch (Exception e) {
            LOGGER.error("Failed to read file: {}", filePathInResources, e);
            throw new FileReadingException("Failed to read file " + filePathInResources
                    + ". Make sure this file exists.");
        }

        LOGGER.debug("Read entire file of length {} from: {}", content.length(), filePathInResources);
        return content.toString().strip();
    }

}
