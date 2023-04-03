package ua.foxminded.schoolapp.datageneration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CoursesReader {

    public Set<String> readCoursesFrom(String fileName) {
        Set<String> courses = new HashSet<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("courses/" + fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            courses = reader.lines().collect(Collectors.toSet());
        } catch (Exception e) {
            System.err.println("Failed to read file " + fileName);
            e.printStackTrace();
        }
        return courses;
    }

}
