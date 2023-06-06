package ua.foxminded.schoolapp.datasetup;

import java.util.List;
import java.util.stream.IntStream;
import ua.foxminded.schoolapp.model.Course;

public class CoursesGeneratorTestHelper {
    
    public List<Course> getTestListOfCourses(int numberOfCourses) {
        return IntStream.rangeClosed(1, numberOfCourses)
                        .mapToObj(i -> new Course("CourseName_" + i, "CourseDescription_" + i))
                        .toList();
    }
    
    public List<String> getTestListOf(String switcher, int numberCoursesNamesOrDescriptions) {
        String courseNameOrDescription;

        if ("courses_names".equals(switcher) && numberCoursesNamesOrDescriptions > 0) {
            courseNameOrDescription = "Course_Name_";
        } else if ("courses_descriptions".equals(switcher) && numberCoursesNamesOrDescriptions > 0) {
            courseNameOrDescription = "Course_Description_";
        } else {
            throw new IllegalArgumentException();
        }

        return IntStream.rangeClosed(1, numberCoursesNamesOrDescriptions)
                        .mapToObj(i -> courseNameOrDescription + i)
                        .toList();
    }

}
