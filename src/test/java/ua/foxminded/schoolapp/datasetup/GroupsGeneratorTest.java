package ua.foxminded.schoolapp.datasetup;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.model.Group;
import java.lang.reflect.Method;
import java.util.List;

class GroupsGeneratorTest {

    Generatable<Group> groupsGenerator;

    @BeforeEach
    void setUp() {
        groupsGenerator = new GroupsGenerator();
    }

    @Test
    void toGenerate_shouldListWithRandomGroups_whenInvokeToGenerate() {
        String groupNameRegex = "^[A-Z]{2}-[0-9]{2}$";

        List<Group> groups = groupsGenerator.toGenerate();

        for (Group group : groups) {
            assertTrue(group.getGroupName().matches(groupNameRegex));
        }
    }

    @Test
    void toGenerate_shouldListWithSizeTen_whenInvokeToGenerate() {
        int expectedSize = 10;

        int actualSize = groupsGenerator.toGenerate().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void createRandomInitials_shouldRandomInitials_whenInvokeCreateRandomInitials() throws Exception {
        String groupNameRegex = "^[A-Z]{2}$";
        Method method = GroupsGenerator.class.getDeclaredMethod("createRandomInitials");
        method.setAccessible(true);

        String groupInitials = method.invoke(groupsGenerator).toString();

        assertTrue(groupInitials.matches(groupNameRegex));
    }

    @Test
    void createTwoRandomDigits_should_when() throws Exception {
        String randomDigitsRegex = "^[0-9]{2}$";
        Method method = GroupsGenerator.class.getDeclaredMethod("createTwoRandomDigits");
        method.setAccessible(true);

        String twoRandomDigits = method.invoke(groupsGenerator).toString();

        assertTrue(twoRandomDigits.matches(randomDigitsRegex));
    }

}
