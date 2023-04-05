package ua.foxminded.schoolapp.datageneration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;
import ua.foxminded.schoolapp.dto.Course;

public class CoursesGenerator {

    public List<Course> getCourses() {
        List<String> courses = readCoursesFrom("courses.txt");

        return IntStream.rangeClosed(1, 10)
                        .mapToObj(i -> new Course(i, courses.get(i - 1), "Description"))
                        .toList();
    }

    private List<String> readCoursesFrom(String fileName) {
        List<String> courses = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("courses/" + fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            courses = reader.lines()
                            .toList();
        } catch (Exception e) {
            System.err.println("Failed to read file " + fileName);
            e.printStackTrace();
        }
        return courses;
    }

}
