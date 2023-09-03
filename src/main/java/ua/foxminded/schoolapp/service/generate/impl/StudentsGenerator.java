package ua.foxminded.schoolapp.service.generate.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.generate.Generatable;
import ua.foxminded.schoolapp.service.generate.Reader;
import ua.foxminded.schoolapp.exception.DataGenerationException;

/**
 * Generates a list of randomly generated student objects. The StudentsGenerator
 * class is an implementation of the {@link Generatable} interface.
 * <p>
 * This class is annotated with {@code @Component} to indicate that it is a
 * Spring component, and it can be automatically discovered and registered as a
 * bean in the Spring context. The StudentsGenerator generates a list of 200
 * randomly generated {@link Student} objects, where each student has a unique
 * first name, last name, and a random group ID. The first names and last names
 * are read from files specified by the file paths "students/first_names.txt"
 * and "students/last_names.txt" respectively, and they must have at least 20
 * entries each. The group IDs are randomly generated and must not exceed 10
 * different group IDs.
 *
 * @author Serhii Bohdan
 */
@Component
public class StudentsGenerator implements Generatable<Student> {

    /**
     * The number of students to generate.
     */
    private static final int NUMBER_OF_STUDENTS = 200;

    /**
     * The minimum number of distinct student names required for generation.
     */
    private static final int MIN_NUMBER_OF_STUDENT_NAMES = 20;

    /**
     * The logger for logging events and messages in the {@link StudentsGenerator}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentsGenerator.class);

    /**
     * The Random object used for generating random values.
     */
    private static final Random RANDOM = new Random();

    private final Reader reader;

    /**
     * Constructs a StudentsGenerator object with the specified Reader.
     *
     * @param reader the Reader object used to read data.
     */
    public StudentsGenerator(Reader reader) {
        this.reader = reader;
    }

    /**
     * Generates a list of randomly generated Student objects.
     *
     * @return a list of randomly generated Student objects.
     */
    public List<Student> toGenerate() {
        LOGGER.info("Generating students started...");
        List<String[]> studentsNames = getStudentsFullName();
        List<Integer> randomGroupIds = getRandomGroupIds();

        List<Student> generatedStudents = IntStream.rangeClosed(0, NUMBER_OF_STUDENTS - 1)
                .mapToObj(i -> {
                    String firstName = studentsNames.get(i)[0];
                    String lastName = studentsNames.get(i)[1];
                    return new Student(firstName, lastName, randomGroupIds.get(i));
                 })
                .toList();

        LOGGER.info("Generated {} students.", generatedStudents.size());
        LOGGER.debug("Generated students: {}", generatedStudents);
        return generatedStudents;
    }

    private List<String[]> getStudentsFullName() {
        LOGGER.debug("Getting students' full names");
        List<String> firstNames = reader.readFileAndPopulateListWithLines("students/first_names.txt");
        List<String> lastNames = reader.readFileAndPopulateListWithLines("students/last_names.txt");

        if (firstNames.size() >= MIN_NUMBER_OF_STUDENT_NAMES && firstNames.size() == lastNames.size()) {
            return Stream.generate(() -> firstNames.get(RANDOM.nextInt(MIN_NUMBER_OF_STUDENT_NAMES)) + " " 
                   + lastNames.get(RANDOM.nextInt(MIN_NUMBER_OF_STUDENT_NAMES)))
                   .distinct()
                   .limit(NUMBER_OF_STUDENTS)
                   .map(fullName -> fullName.split(" "))
                   .toList();
        } else {
            LOGGER.error("An error occurred during students' names generation.");
            throw new DataGenerationException("An error occurred during the generation of Students data. The number of "
                    + "names and surnames in the files must be equal and not less than twenty.");
        }
    }

    private List<Integer> getRandomGroupIds() {
        LOGGER.debug("Generating random group IDs for students");
        List<Integer> randomGroupIds = new ArrayList<>();
        int maxCountStudentsInGroup = 30;
        int minCountStudentsInGroup = 10;

        while(randomGroupIds.size() < NUMBER_OF_STUDENTS) {
            int randomGroupId = RANDOM.nextInt(1, 11);

            if(Collections.frequency(randomGroupIds, randomGroupId) < maxCountStudentsInGroup) {
                randomGroupIds.add(randomGroupId);
            }

            if((Collections.frequency(randomGroupIds, randomGroupId) < minCountStudentsInGroup)) {
                randomGroupIds.add(randomGroupId);
            }
        }

        Collections.shuffle(randomGroupIds);
        LOGGER.debug("Generated random group IDs: {}", randomGroupIds);
        return randomGroupIds;
    }

}
