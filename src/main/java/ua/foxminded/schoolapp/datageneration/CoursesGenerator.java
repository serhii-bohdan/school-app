package ua.foxminded.schoolapp.datageneration;

import java.util.List;
import java.util.stream.IntStream;
import ua.foxminded.schoolapp.entity.Course;

public class CoursesGenerator {

    Reader reader = new Reader();

    public List<Course> getCourses() {
        List<String> coursesNames = returnCoursesNames();
        List<String> coursesDescriptions = returnCoursesDescriptions();

        return IntStream.rangeClosed(1, 10)
                        .mapToObj(i -> new Course(coursesNames.get(i - 1), coursesDescriptions.get(i - 1)))
                        .toList();
    }

    private List<String> returnCoursesNames() {
        List<String> coursesNames = reader.readFileAndPopulateList("courses/courses.txt");

        return IntStream.rangeClosed(0, 9)
                        .mapToObj(coursesNames::get)
                        .toList();
    }

    private List<String> returnCoursesDescriptions() {
        List<String> coursesDescriptions = reader.readFileAndPopulateList("courses/descriptions.txt");

        return IntStream.rangeClosed(0, 9)
                        .mapToObj(coursesDescriptions::get)
                        .toList();
    }

}
