package ua.foxminded.schoolapp.service.generate.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.service.generate.Generatable;
import java.lang.reflect.Method;
import java.util.List;

@SpringBootTest(classes = { GroupsGenerator.class })
class GroupsGeneratorTest {

    @Autowired
    private Generatable<GroupDto> groupsGenerator;

    @Test
    void toGenerate_shouldListWithRandomGroups_whenInvokeToGenerate() {
        String groupNameRegex = "^[A-Z]{2}-[0-9]{2}$";

        List<GroupDto> groups = groupsGenerator.toGenerate();

        for (GroupDto group : groups) {
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
    void getRandomInitials_shouldRandomInitials_whenInvokeGetRandomInitials() throws Exception {
        String groupNameRegex = "^[A-Z]{2}$";
        Method method = GroupsGenerator.class.getDeclaredMethod("getRandomInitials");
        method.setAccessible(true);

        String groupInitials = method.invoke(groupsGenerator).toString();

        assertTrue(groupInitials.matches(groupNameRegex));
    }

    @Test
    void getTwoRandomDigits_shouldTwoRandomDigitsFromZeroToNineInclusive_whenInvokeGetTwoRandomDigits()
            throws Exception {
        String randomDigitsRegex = "^[0-9]{2}$";
        Method method = GroupsGenerator.class.getDeclaredMethod("getTwoRandomDigits");
        method.setAccessible(true);

        String twoRandomDigits = method.invoke(groupsGenerator).toString();

        assertTrue(twoRandomDigits.matches(randomDigitsRegex));
    }

}
