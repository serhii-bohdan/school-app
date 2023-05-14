package ua.foxminded.schoolapp.datasetup;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import ua.foxminded.schoolapp.exception.DataSetUpException;
import ua.foxminded.schoolapp.model.Course;

public class CoursesGenerator implements Generatable<Course> {

    private Reader reader;

    public CoursesGenerator(Reader reader) {
        Objects.requireNonNull(reader);
        this.reader = reader;
    }

    public List<Course> toGenerate() {
        List<String> coursesNames = reader.readFileAndPopulateList("courses/courses.txt");
        List<String> coursesDescriptions = reader.readFileAndPopulateList("courses/descriptions.txt");

        if(coursesNames.size() >= 10 && coursesNames.size() == coursesDescriptions.size()) {
            return IntStream.rangeClosed(0, 9)
                            .mapToObj(i -> new Course(coursesNames.get(i), coursesDescriptions.get(i)))
                            .toList();
        } else {
            throw new DataSetUpException("Error generating courses. The number of course names must be"
                    + " equal to or greater than ten. Also, make sure that the number of course names "
                    + "equals the number of descriptions.");
        }
    }

}
