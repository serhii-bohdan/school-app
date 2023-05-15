package ua.foxminded.schoolapp.datasetup.impl;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.model.Group;

public class GroupsGenerator implements Generatable<Group> {

    private static final String SEPARATOR = "-";

    private Random random = new Random();

    public List<Group> toGenerate() {
        return Stream.generate(() -> createRandomInitials() + SEPARATOR + createTwoRandomDigits())
                     .distinct()
                     .limit(10)
                     .map(Group::new)
                     .toList();
    }

    private String createRandomInitials() {
        StringBuilder initials = new StringBuilder();
        initials.append((char) (random.nextInt(26) + 'A'));
        initials.append((char) (random.nextInt(26) + 'A'));
        return initials.toString();
    }

    private String createTwoRandomDigits() {
        StringBuilder twoRandomDigits = new StringBuilder();
        twoRandomDigits.append(random.nextInt(10));
        twoRandomDigits.append(random.nextInt(10));
        return twoRandomDigits.toString();
    }

}
