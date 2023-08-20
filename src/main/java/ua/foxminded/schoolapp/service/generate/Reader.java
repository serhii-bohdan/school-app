package ua.foxminded.schoolapp.service.generate;

import java.util.List;

/**
 * Interface for reading file contents.
 * 
 * @author Serhii Bohdan
 */
public interface Reader {

    /**
     * Reads the content of a file and populates a list with its lines.
     *
     * @param filePathInResources the path to the file in the project's resources.
     * @return a list of strings containing the lines of the file.
     */
    List<String> readFileAndPopulateListWithLines(String filePathInResources);

    /**
     * Reads the entire content of a file and returns it as a single string.
     *
     * @param filePathInResources the path to the file in the project's resources.
     * @return a string containing the entire content of the file.
     */
    String readAllFileToString(String filePathInResources);

}
