package ua.foxminded.schoolapp.service.generate.impl;

import java.util.List;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.exception.DataGenerationException;
import ua.foxminded.schoolapp.service.generate.Generatable;
import ua.foxminded.schoolapp.service.generate.Reader;

/**
 * The CoursesGenerator class is responsible for generating a list of course Dto
 * objects with names and descriptions. CoursesGenerator class is an
 * implementation of the {@link Generatable} interface.
 * <p>
 * This class is annotated with {@code @Component} to indicate that it is a
 * Spring component, and it can be automatically discovered and registered as a
 * bean in the Spring context. The CoursesGenerator uses a {@link Reader} to
 * read course names and descriptions from files, and then generates a list of
 * {@link CourseDto} objects based on the read data.
 *
 * @author Serhii Bohdan
 */
@Component
public class CoursesGenerator implements Generatable<CourseDto> {

    /**
     * The minimum number of courses to generate.
     */
    private static final int NUMBER_OF_COURSES = 10;

    /**
     * The logger for logging events and messages in the {@link CoursesGenerator}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CoursesGenerator.class);

    private final Reader reader;

    /**
     * Constructs a CoursesGenerator object with the specified reader.
     *
     * @param reader the reader used to read course names and descriptions.
     */
    public CoursesGenerator(Reader reader) {
        this.reader = reader;
    }

    /**
     * Generates a list of course Dto objects with readed from files names and
     * descriptions.
     *
     * @return a list of course Dto objects.
     */
    @Override
    public List<CourseDto> toGenerate() {
        LOGGER.info("Generating courses started...");
        List<String> coursesNames = reader.readFileAndPopulateListWithLines("courses/courses.txt");
        List<String> coursesDescriptions = reader.readFileAndPopulateListWithLines("courses/descriptions.txt");

        if (coursesNames.size() >= NUMBER_OF_COURSES && coursesNames.size() == coursesDescriptions.size()) {
            List<CourseDto> generatedCourses = IntStream.rangeClosed(0, NUMBER_OF_COURSES - 1)
                    .mapToObj(i -> new CourseDto(coursesNames.get(i), coursesDescriptions.get(i)))
                    .toList();

            LOGGER.info("Generated {} courses.", generatedCourses.size());
            LOGGER.debug("Generated courses: {}", generatedCourses);
            return generatedCourses;
        } else {
            LOGGER.error("Error generating courses. The number of course names must be equal to or greater than ten.");
            throw new DataGenerationException("Error generating courses. The number of course names must be"
                    + " equal to or greater than ten. Also, make sure that the number of course names "
                    + "equals the number of descriptions.");
        }
    }

}
