package ua.foxminded.schoolapp.datageneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import ua.foxminded.schoolapp.entity.Student;

public class StudentsGenerator {

    private Random random = new Random();
    private Reader reader = new Reader();

    public List<Student> getStudents() {
        List<String[]> nameOfStudents = generateStudents();
        List<Integer> randomGroupIds = generateRandomGroupIds();

        return IntStream.rangeClosed(1, 200)
                        .mapToObj(i -> {
                            String firstName = nameOfStudents.get(i - 1)[0];
                            String lastName = nameOfStudents.get(i - 1)[1];
                            return new Student(randomGroupIds.get(i - 1), firstName, lastName);
                         })
                        .toList();
    }

    private List<String[]> generateStudents() {
        List<String> firstNames = reader.readFileAndPopulateList("students/first_names.txt");
        List<String> lastNames = reader.readFileAndPopulateList("students/last_names.txt");

        return Stream.generate(() -> firstNames.get(random.nextInt(20)) + " " + lastNames.get(random.nextInt(20)))
                     .distinct()
                     .limit(200)
                     .map(fullName -> fullName.split(" "))
                     .toList();
    }

    public List<Integer> generateRandomGroupIds() {
        List<Integer> randomGroupIds = new ArrayList<>();
        int maxCountStudentsInGroup = 30;
        int minCountStudentsInGroup = 10;

        while(randomGroupIds.size() < 200) {
            int randomGroupId = random.nextInt(1, 11);

            if(Collections.frequency(randomGroupIds, randomGroupId) <= maxCountStudentsInGroup) {
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
