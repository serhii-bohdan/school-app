package ua.foxminded.schoolapp.datageneration;

import java.util.List;
import java.util.stream.IntStream;
import ua.foxminded.schoolapp.entity.Course;

public class CoursesGenerator {

    Reader reader = new Reader();
    
    public List<Course> getCourses() {
        List<String> coursesNames = getCourses("Names");
        List<String> coursesDescriptions = getCourses("Descriptions");

        return IntStream.rangeClosed(1, 10)
                        .mapToObj(i -> new Course(i, coursesNames.get(i - 1), coursesDescriptions.get(i - 1)))
                        .toList();
    }

    private List<String> getCourses(String switcher) {
        List<String> coursesNamesWithDescriptions = reader.readFileAndPopulateList("courses/courses.txt");
        int index;

        if("Names".equals(switcher)) {
            index = 0;
        } else if("Descriptions".equals(switcher)) {
            index = 1;
        } else {
            throw new IllegalArgumentException("Only the following lines can be passed to the argument:"
                    + " \"Names\", \"Descriptions\".");
        }
        return IntStream.rangeClosed(0, 9)
                        .mapToObj(i -> coursesNamesWithDescriptions.get(i).split("_")[index])
                        .toList();
    }

}
