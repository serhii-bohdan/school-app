package ua.foxminded.schoolapp.datasetup;

import java.util.List;

/**
 * Interface for generating data of a specific type.
 *
 * @param <T> the type of data to generate.
 * @author Serhii Bohdan
 */
public interface Generatable<T> {

    /**
     * Generates a list of data objects.
     *
     * @return a list of generated data objects.
     */
    List<T> toGenerate();

}
