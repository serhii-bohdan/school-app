package ua.foxminded.schoolapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolapp.model.Course;

/**
 * The CourseRepository interface provides operations for accessing and
 * manipulating Course entities. It extends the Spring Data
 * {@link JpaRepository} interface, allowing for easy interaction with the
 * database. This repository is annotated with {@code @Repository}, indicating
 * that it is a Spring bean responsible for database access.
 *
 * @author Serhii Bohdan
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    /**
     * Finds a course in the database by its name.
     *
     * @param courseName the name of the course to find
     * @return an {@link Optional} containing the found course, or empty if not
     *         found
     */
    Optional<Course> findByCourseName(String courseName);

}
