package ua.foxminded.schoolapp.dao;

import java.util.Optional;
import ua.foxminded.schoolapp.model.Student;

/**
 * The StudentDao interface provides operations for accessing and manipulating
 * Student entities. It extends the {@link BaseDao} interface.
 *
 * @author Serhii Bohdan
 */
public interface StudentDao extends BaseDao<Student> {

    /**
     * Finds a student by their full name.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @return an {@link Optional} containing the found student, or empty if not
     *         found
     */
    Optional<Student> findStudentByFullName(String firstName, String lastName);

}
