package ua.foxminded.schoolapp.service.generate.impl;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolapp.service.generate.Generatable;
import ua.foxminded.schoolapp.service.generate.Reader;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.exception.DataGenerationException;

/**
 * Generates a list of randomly generated student Dto objects. The
 * StudentsGenerator class is an implementation of the {@link Generatable}
 * interface.
 * <p>
 * This class is annotated with {@code @Component} to indicate that it is a
 * Spring component, and it can be automatically discovered and registered as a
 * bean in the Spring context. The StudentsGenerator generates a list of 200
 * randomly generated {@link StudentDto} objects, where each student has a
 * unique first name, last name. The first names and last names are read from
 * files specified by the file paths "students/first_names.txt" and
 * "students/last_names.txt" respectively, and they must have at least 20
 * entries each.
 *
 * @author Serhii Bohdan
 */
@Component
public class StudentsGenerator implements Generatable<StudentDto> {

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
     * Generates a list of randomly generated Student Dto objects.
     *
     * @return a list of randomly generated Student Dto objects.
     */
    public List<StudentDto> toGenerate() {
        LOGGER.info("Generating students started...");
        List<String[]> studentsNames = getStudentsFullName();

        List<StudentDto> generatedStudents = IntStream.rangeClosed(0, NUMBER_OF_STUDENTS - 1)
                .mapToObj(i -> {
                    StudentDto student = new StudentDto();
                    student.setFirstName(studentsNames.get(i)[0]);
                    student.setLastName(studentsNames.get(i)[1]);
                    return student;
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

}
