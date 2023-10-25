package ua.foxminded.schoolapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolapp.model.Group;

/**
 * The GroupRepository interface provides operations for accessing and
 * manipulating Group entities. It extends the Spring Data {@link JpaRepository}
 * interface, which enables easy interaction with the database. This repository
 * is annotated with {@code @Repository}, indicating that it is a Spring bean
 * responsible for database access.
 *
 * @author Serhii Bohdan
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    /**
     * Finds a group in the database by its name.
     *
     * @param groupName the name of the group to find
     * @return an {@link Optional} containing the found group, or empty if not found
     */
    Optional<Group> findByGroupName(String groupName);

}
