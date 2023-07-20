package ua.foxminded.schoolapp.datasetup.impl;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.model.Group;

/**
 * Generates a list of randomly generated group objects. The GroupsGenerator
 * class is an implementation of the {@link Generatable} interface.
 *
 * @author Serhii Bohdan
 */
public class GroupsGenerator implements Generatable<Group> {

    /**
     * The separator used to separate the initials and random digits in the group
     * name.
     */
    public static final String SEPARATOR = "-";

    /**
     * The Random object used for generating random values.
     */
    private Random random = new Random();

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
