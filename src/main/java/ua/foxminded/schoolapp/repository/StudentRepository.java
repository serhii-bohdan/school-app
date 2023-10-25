package ua.foxminded.schoolapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolapp.model.Student;

/**
 * The StudentRepository interface provides operations for accessing and
 * manipulating Student entities. It extends the Spring Data
 * {@link JpaRepository} interface, which enables easy interaction with the
 * database. This repository is annotated with {@code @Repository}, indicating
 * that it is a Spring bean responsible for database access.
 *
 * @author Serhii Bohdan
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    /**
     * Finds a student by their first name and last name.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @return an {@link Optional} containing the found student, or empty if not
     *         found
     */
    Optional<Student> findByFirstNameAndLastName(String firstName, String lastName);

}
