package ua.foxminded.schoolapp.datasetup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.exception.DataSetUpException;

public class StudentsGenerator implements Generatable<Student> {

    private Random random = new Random();
    private Reader reader;

    public StudentsGenerator(Reader reader) {
        Objects.requireNonNull(reader);
        this.reader = reader;
    }

    public List<Student> toGenerate() {
        List<String[]> studentsNames = generateStudentsFullName();
        List<Integer> randomGroupIds = generateRandomGroupIds();

        return IntStream.rangeClosed(0, 199)
                        .mapToObj(i -> {
                            String firstName = studentsNames.get(i)[0];
                            String lastName = studentsNames.get(i)[1];
                            return new Student(firstName, lastName, randomGroupIds.get(i));
                         })
                        .toList();
    }

    private List<String[]> generateStudentsFullName() {
        List<String> firstNames = reader.readFileAndPopulateList("students/first_names.txt");
        List<String> lastNames = reader.readFileAndPopulateList("students/last_names.txt");

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

    private List<Integer> generateRandomGroupIds() {
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
