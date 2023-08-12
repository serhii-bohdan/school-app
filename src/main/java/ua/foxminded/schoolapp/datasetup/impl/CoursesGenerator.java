package ua.foxminded.schoolapp.datasetup.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.datasetup.Reader;
import ua.foxminded.schoolapp.exception.DataSetUpException;
import ua.foxminded.schoolapp.model.Course;

/**
 * The CoursesGenerator class is responsible for generating a list of course
 * objects with names and descriptions. CoursesGenerator class is an
 * implementation of the {@link Generatable} interface.
 * <p>
 * This class is annotated with {@code @Component} to indicate that it is a
 * Spring component, and it can be automatically discovered and registered as a
 * bean in the Spring context. The CoursesGenerator uses a {@link Reader} to
 * read course names and descriptions from files, and then generates a list of
 * {@link Course} objects based on the read data.
 *
 * @author Serhii Bohdan
 */
@Component
public class CoursesGenerator implements Generatable<Course> {

    private Reader reader;

    /**
     * Constructs a CoursesGenerator object with the specified reader.
     *
     * @param reader the reader used to read course names and descriptions.
     */
    public CoursesGenerator(Reader reader) {
        Objects.requireNonNull(reader, "reader must not be null");
        this.reader = reader;
    }

    /**
     * Generates a list of course objects with readed from files names and
     * descriptions.
     *
     * @return a list of course objects.
     */
    @Override
    public List<Course> toGenerate() {
        List<String> coursesNames = reader.readFileAndPopulateListWithLines("courses/courses.txt");
        List<String> coursesDescriptions = reader.readFileAndPopulateListWithLines("courses/descriptions.txt");

        if (coursesNames.size() >= 10 && coursesNames.size() == coursesDescriptions.size()) {
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
