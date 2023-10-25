package ua.foxminded.schoolapp.service.logic.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.mapper.CourseMapper;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.repository.CourseRepository;
import ua.foxminded.schoolapp.service.generate.Generatable;
import ua.foxminded.schoolapp.service.logic.CourseService;

/**
 * The CourseServiceImpl class is an implementation of the {@link CourseService}
 * interface. It provides operations for managing courses, including
 * initializing courses with a generator, retrieving all courses, and getting
 * courses associated with a specific student.
 * <p>
 * This class is annotated with {@code @Service} to indicate that it is a Spring
 * service, and it can be automatically discovered and registered as a bean in
 * the Spring context. The CourseServiceImpl requires a {@link Generatable}
 * object to generate courses Dto and a {@link CourseRepository} object to
 * access the course data.
 * 
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    /**
     * The logger for logging events and messages in the {@link CourseServiceImpl}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final Generatable<CourseDto> coursesGenerator;
    private final CourseRepository courseRepository;

    /**
     * Constructs a new CourseServiceImpl with the specified courses generator and
     * course repository.
     *
     * @param coursesGenerator an instance of {@link Generatable} for generating
     *                         courses
     * @param courseRepository an instance of {@link CourseRepository} for accessing
     *                         and managing course data
     */
    public CourseServiceImpl(Generatable<CourseDto> coursesGenerator, CourseRepository courseRepository) {
        this.coursesGenerator = coursesGenerator;
        this.courseRepository = courseRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initCourses() {
        LOGGER.info("Filling with generated courses");
        coursesGenerator.toGenerate().stream()
                .map(CourseMapper::mapDtoToCourse)
                .forEach(courseRepository::save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Course> addCourse(CourseDto newCourse) {
        Course course = CourseMapper.mapDtoToCourse(newCourse);
        LOGGER.debug("Adding a new course: {}", course);

        return Optional.ofNullable(courseRepository.save(course));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Course> getCourseById(Integer courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        LOGGER.debug("Search course by ID {}: {}", courseId, course);

        return course;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Course> getCourseByName(String courseName) {
        Optional<Course> course = courseRepository.findByCourseName(courseName);
        LOGGER.debug("Search course by name {}: {}", courseName, course);

        return course;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> getAllCourses() {
        List<Course> allCourses = courseRepository.findAll();
        LOGGER.debug("All received courses: {}", allCourses);

        return allCourses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Course> updateCourse(Course updatedCourse) {
        LOGGER.debug("Updating course data: {}", updatedCourse);
        Course course = courseRepository.save(updatedCourse);

        return Optional.ofNullable(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCourseByName(String courseName) {
        Optional<Course> course = getCourseByName(courseName);

        if (course.isPresent()) {
            LOGGER.debug("Deleting course: {}", course);
            courseRepository.delete(course.get());
        }
    }

}
