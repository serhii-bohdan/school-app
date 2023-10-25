package ua.foxminded.schoolapp.dto.mapper;

import org.modelmapper.ModelMapper;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.model.Course;

/**
 * The CourseMapper class is responsible for mapping between {@link Course} and
 * {@link CourseDto} objects. It uses the {@link ModelMapper} library to perform
 * the mapping.
 * </p>
 *
 * @author Serhii Bohdan
 */
public class CourseMapper {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private CourseMapper() {
    }

    /**
     * Maps a {@link Course} object to a {@link CourseDto} object.
     *
     * @param course the Course object to map
     * @return the mapped CourseDto object
     */
    public static CourseDto mapCourseToDto(Course course) {
        return MODEL_MAPPER.map(course, CourseDto.class);
    }

    /**
     * Maps a {@link CourseDto} object to a {@link Course} object.
     *
     * @param courseDto the CourseDto object to map
     * @return the mapped Course object
     */
    public static Course mapDtoToCourse(CourseDto courseDto) {
        return MODEL_MAPPER.map(courseDto, Course.class);
    }

}
