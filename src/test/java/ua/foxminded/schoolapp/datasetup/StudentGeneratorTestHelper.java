package ua.foxminded.schoolapp.datasetup;

import java.util.List;
import java.util.stream.IntStream;

public class StudentGeneratorTestHelper {

    public List<String> getTestListOf(String switcher, int numbersOfNameToGenerate) {
        String firstOrLastName;

        if ("first_names".equals(switcher) && numbersOfNameToGenerate > 0) {
            firstOrLastName = "First_Name_";
        } else if ("last_names".equals(switcher) && numbersOfNameToGenerate > 0) {
            firstOrLastName = "Last_Name_";
        } else {
            throw new IllegalArgumentException();
        }

        return IntStream.rangeClosed(1, numbersOfNameToGenerate)
                        .mapToObj(i -> firstOrLastName + i)
                        .toList();
    }

}
