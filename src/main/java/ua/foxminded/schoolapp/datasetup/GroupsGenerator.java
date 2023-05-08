package ua.foxminded.schoolapp.datasetup;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ua.foxminded.schoolapp.model.Group;

public class GroupsGenerator implements Generatable<Group> {

    private static final String SEPARATOR = "-";

    private Random random = new Random();

    public List<Group> toGenerate() {
        List<String> groupsNames = generateGroupsNames();

        return IntStream.rangeClosed(1, 10)
                        .mapToObj(i -> new Group(groupsNames.get(i - 1)))
                        .toList();
    }

    private List<String> generateGroupsNames() {
        return Stream.generate(() -> createRandomInitials() + SEPARATOR + createRandomDigits())
                     .distinct()
                     .limit(10)
                     .toList();
    }

    private String createRandomInitials() {
        StringBuilder initials = new StringBuilder();
        initials.append((char) (random.nextInt(26) + 'A'));
        initials.append((char) (random.nextInt(26) + 'A'));
        return initials.toString();
    }

    private String createRandomDigits() {
        StringBuilder digits = new StringBuilder();
        digits.append(random.nextInt(10));
        digits.append(random.nextInt(10));
        return digits.toString();
    }

}
