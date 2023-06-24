package ua.foxminded.schoolapp.datasetup.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.datasetup.Reader;
import ua.foxminded.schoolapp.exception.DataSetUpException;

/**
 * Generates a list of randomly generated student objects.
 *
 * @author Serhii Bohdan
 */
public class StudentsGenerator implements Generatable<Student> {

    /**
     * The Random object used for generating random values.
     */
    private Random random = new Random();

    /**
     * The Reader object used for reading data.
     */
    private Reader reader;

    /**
     * Constructs a StudentsGenerator object with the specified Reader.
     *
     * @param reader the Reader object used to read data.
     * @throws NullPointerException if the reader is null.
     */
    public StudentsGenerator(Reader reader) {
        Objects.requireNonNull(reader);
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

    /**
     * Generates a list of arrays containing randomly generated student names.
     *
     * @return a list of arrays containing randomly generated student names.
     * @throws DataSetUpException if the number of names of students is less than twenty, or the 
     *                            number of first names is not equal to the number of last names.
     */
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

    /**
     * Generates a list of random group IDs.
     *
     * @return a list of random group IDs.
     */
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
