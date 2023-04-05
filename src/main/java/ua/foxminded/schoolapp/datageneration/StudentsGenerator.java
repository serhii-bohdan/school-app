package ua.foxminded.schoolapp.datageneration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import ua.foxminded.schoolapp.dto.Student;

public class StudentsGenerator {

    private Random random = new Random();
    
    public List<Student> getStudents() {
        List<String[]> nameOfStudents = generateStudents();        

        return IntStream.rangeClosed(1, 200)
                        .mapToObj(i -> {
                            String firstName = nameOfStudents.get(i - 1)[0];
                            String lastName = nameOfStudents.get(i - 1)[1];
                            return new Student(i, generateRandomGroupId(), firstName, lastName);
                         })
                        .toList();
    }
    
    
    private List<String[]> generateStudents() {
        List<String> firstNames = read("first_names.txt");
        List<String> lastNames = read("last_names.txt");
        
        return Stream.generate(() -> firstNames.get(random.nextInt(20)) + " " + lastNames.get(random.nextInt(20)))
                     .distinct()
                     .limit(200)
                     .map(fullName -> fullName.split(" "))
                     .toList();
    }

    private int generateRandomGroupId() {
        return random.nextInt(1, 11);
    }

    private List<String> read(String fileName) {
        List<String> names = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("students/" + fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            names = reader.lines().toList();
        } catch (Exception e) {
            System.err.println("Failed to read file " + fileName);
            e.printStackTrace();
        }
        return names;
    }

}
