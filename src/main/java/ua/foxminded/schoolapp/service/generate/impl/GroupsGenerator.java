package ua.foxminded.schoolapp.service.generate.impl;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.service.generate.Generatable;

/**
 * Generates a list of randomly generated group objects. The GroupsGenerator
 * class is an implementation of the {@link Generatable} interface.
 * <p>
 * This class is annotated with {@code @Component} to indicate that it is a
 * Spring component, and it can be automatically discovered and registered as a
 * bean in the Spring context. The GroupsGenerator generates a list of
 * {@link Group} objects, where each group has a randomly generated name
 * consisting of two uppercase letters as initials and two random digits,
 * separated by a {@link #SEPARATOR}.
 *
 * @author Serhii Bohdan
 */
@Component
public class GroupsGenerator implements Generatable<Group> {

    /**
     * The separator used to separate the initials and random digits in the group
     * name.
     */
    public static final String SEPARATOR = "-";

    /**
     * The Random object used for generating random values.
     */
    private final Random random = new Random();

    /**
     * Generates a list of randomly generated Group objects.
     *
     * @return a list of randomly generated Group objects.
     */
    @Override
    public List<Group> toGenerate() {
        return Stream.generate(() -> getRandomInitials() + SEPARATOR + getTwoRandomDigits())
                .distinct().limit(10)
                .map(Group::new)
                .toList();
    }

    private String getRandomInitials() {
        StringBuilder initials = new StringBuilder();
        initials.append((char) (random.nextInt(26) + 'A'));
        initials.append((char) (random.nextInt(26) + 'A'));
        return initials.toString();
    }

    private String getTwoRandomDigits() {
        StringBuilder twoRandomDigits = new StringBuilder();
        twoRandomDigits.append(random.nextInt(10));
        twoRandomDigits.append(random.nextInt(10));
        return twoRandomDigits.toString();
    }

}
