package ua.foxminded.schoolapp.datageneration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentsGeneration {

    Random random = new Random();
    
    public Set<String> generateStudents() {
        List<String> firstNames = read("first_names.txt");
        List<String> lastNames = read("last_names.txt");
        
        return Stream.generate(() -> firstNames.get(random.nextInt(20)) + " " + lastNames.get(random.nextInt(20)))
                     .distinct()
                     .limit(200)
                     .collect(Collectors.toSet());
        
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
