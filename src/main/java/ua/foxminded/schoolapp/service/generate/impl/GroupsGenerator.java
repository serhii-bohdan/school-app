package ua.foxminded.schoolapp.service.generate.impl;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.service.generate.Generatable;

/**
 * Generates a list of randomly generated group Dto objects. The GroupsGenerator
 * class is an implementation of the {@link Generatable} interface.
 * <p>
 * This class is annotated with {@code @Component} to indicate that it is a
 * Spring component, and it can be automatically discovered and registered as a
 * bean in the Spring context. The GroupsGenerator generates a list of
 * {@link GroupDto} objects, where each group has a randomly generated name
 * consisting of two uppercase letters as initials and two random digits,
 * separated by a {@link #SEPARATOR}.
 *
 * @author Serhii Bohdan
 */
@Component
public class GroupsGenerator implements Generatable<GroupDto> {

    /**
     * The separator used to separate the initials and random digits in the group
     * name.
     */
    private static final String SEPARATOR = "-";

    /**
     * The number of groups to generate.
     */
    private static final int NUMBER_OF_GROUPS = 10;

    /**
     * The logger for logging events and messages in the {@link GroupsGenerator}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupsGenerator.class);

    /**
     * The Random object used for generating random values.
     */
    private static final Random RANDOM = new Random();

    /**
     * Generates a list of randomly generated GroupDto objects.
     *
     * @return a list of randomly generated GroupDto objects.
     */
    @Override
    public List<GroupDto> toGenerate() {
        LOGGER.info("Generating groups started...");

        List<GroupDto> generatedGroups = Stream.generate(() -> getRandomInitials() + SEPARATOR + getTwoRandomDigits())
                .distinct()
                .limit(NUMBER_OF_GROUPS)
                .map(GroupDto::new)
                .toList();

        LOGGER.info("Generated {} groups.", generatedGroups.size());
        LOGGER.debug("Generated groups: {}", generatedGroups);
        return generatedGroups;
    }

    private String getRandomInitials() {
        StringBuilder initials = new StringBuilder();
        initials.append((char) (RANDOM.nextInt(26) + 'A'));
        initials.append((char) (RANDOM.nextInt(26) + 'A'));
        return initials.toString();
    }

    private String getTwoRandomDigits() {
        StringBuilder twoRandomDigits = new StringBuilder();
        twoRandomDigits.append(RANDOM.nextInt(10));
        twoRandomDigits.append(RANDOM.nextInt(10));
        return twoRandomDigits.toString();
    }

}
