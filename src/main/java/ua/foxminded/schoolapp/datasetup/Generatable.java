package ua.foxminded.schoolapp.datasetup;

import java.util.List;

public interface Generatable<T> {

    List<T> toGenerate();

}
