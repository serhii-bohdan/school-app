package ua.foxminded.schoolapp.datasetup;

import java.util.List;
import java.util.stream.IntStream;
import ua.foxminded.schoolapp.model.Group;

public class GroupsGeneratorTestHelper {

    public List<Group> getTestListOfGroups(int numberOfGroups) {
        return IntStream.rangeClosed(1, numberOfGroups)
                        .mapToObj(i -> new Group("YJ-1" + (i - 1)))
                        .toList();
    }

}
