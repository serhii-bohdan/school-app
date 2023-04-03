package ua.foxminded.schoolapp.datageneration;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Random;

public class GroupsGenerator {

    private final String SEPARATOR = "-";
    private Random random = new Random();

    public Set<String> generateUniqueGroups() {
        return Stream.generate(() -> createRandomInitials() + SEPARATOR + createRandomDigits())
                     .distinct()
                     .limit(10)
                     .collect(Collectors.toSet());
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
