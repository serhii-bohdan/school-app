package ua.foxminded.schoolapp.datasetup.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.datasetup.Reader;
import ua.foxminded.schoolapp.exception.DataSetUpException;

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
     * The Random object used for generating random values.
     */
    private Random random = new Random();
    private Reader reader;

    /**
     * Constructs a StudentsGenerator object with the specified Reader.
     *
     * @param reader the Reader object used to read data.
     */
    public StudentsGenerator(Reader reader) {
        Objects.requireNonNull(reader, "reader must not be null");
        this.reader = reader;
    }

    /**
     * Generates a list of randomly generated Student objects.
     *
     * @return a list of randomly generated Student objects.
     */
    public List<Student> toGenerate() {
        List<String[]> studentsNames = getStudentsFullName();
        List<Integer> randomGroupIds = getRandomGroupIds();

        return IntStream.rangeClosed(0, 199)
                        .mapToObj(i -> {
                            String firstName = studentsNames.get(i)[0];
                            String lastName = studentsNames.get(i)[1];
                            return new Student(firstName, lastName, randomGroupIds.get(i));
                         })
                        .toList();
    }

    private List<String[]> getStudentsFullName() {
        List<String> firstNames = reader.readFileAndPopulateListWithLines("students/first_names.txt");
        List<String> lastNames = reader.readFileAndPopulateListWithLines("students/last_names.txt");

        if (firstNames.size() >= 20 && firstNames.size() == lastNames.size()) {
            return Stream.generate(() -> firstNames.get(random.nextInt(20)) + " " + lastNames.get(random.nextInt(20)))
                         .distinct()
                         .limit(200)
                         .map(fullName -> fullName.split(" "))
                         .toList();
        } else {
            throw new DataSetUpException("An error occurred during the generation of Students data. The number of "
                    + "names and surnames in the files must be equal and not less than twenty.");
        }
    }

    private List<Integer> getRandomGroupIds() {
        List<Integer> randomGroupIds = new ArrayList<>();
        int maxCountStudentsInGroup = 30;
        int minCountStudentsInGroup = 10;

        while(randomGroupIds.size() < 200) {
            int randomGroupId = random.nextInt(1, 11);

            if(Collections.frequency(randomGroupIds, randomGroupId) < maxCountStudentsInGroup) {
                randomGroupIds.add(randomGroupId);
            }

            if((Collections.frequency(randomGroupIds, randomGroupId) < minCountStudentsInGroup)) {
                randomGroupIds.add(randomGroupId);
            }
        }
        Collections.shuffle(randomGroupIds);
        return randomGroupIds;
    }

}
