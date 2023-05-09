package ua.foxminded.schoolapp.datasetup;

import java.util.List;

public interface Reader {

    List<String> readFileAndPopulateList(String filePathInResources);

    String readAllFileToString(String filePathInResources);

}
