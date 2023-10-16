package ua.foxminded.schoolapp.service.generate;

import java.util.List;
import java.util.stream.IntStream;
import ua.foxminded.schoolapp.dto.StudentDto;

public class StudentGeneratorTestHelper {

    public List<StudentDto> getTestListOfStudents(int numberOfStudents) {
        return IntStream.rangeClosed(1, numberOfStudents)
                        .mapToObj(i -> new StudentDto("FirstName_" + i, "LastName_" + i))
                        .toList();
    }

    public List<String> getTestListOf(String switcher, int numbersOfNameToGenerate) {
        String firstOrLastName;

        if ("first_names".equals(switcher) && numbersOfNameToGenerate > 0) {
            firstOrLastName = "FirstName_";
        } else if ("last_names".equals(switcher) && numbersOfNameToGenerate > 0) {
            firstOrLastName = "LastName_";
        } else {
            throw new IllegalArgumentException();
        }

        return IntStream.rangeClosed(1, numbersOfNameToGenerate)
                        .mapToObj(i -> firstOrLastName + i)
                        .toList();
    }

}
