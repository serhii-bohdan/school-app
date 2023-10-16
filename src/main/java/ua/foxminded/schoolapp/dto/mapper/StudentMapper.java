package ua.foxminded.schoolapp.dto.mapper;

import org.modelmapper.ModelMapper;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.model.Student;

/**
 * The StudentMapper class is responsible for mapping between {@link Student}
 * and {@link StudentDto} objects. It uses the {@link ModelMapper} library to
 * perform the mapping.
 * </p>
 *
 * @author Serhii Bohdan
 */
public class StudentMapper {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private StudentMapper() {
    }

    /**
     * Maps a {@link Student} object to a {@link StudentDto} object.
     *
     * @param student the Student object to map
     * @return the mapped StudentDto object
     */
    public static StudentDto mapStudentToDto(Student student) {
        return MODEL_MAPPER.map(student, StudentDto.class);
    }

    /**
     * Maps a {@link StudentDto} object to a {@link Student} object.
     *
     * @param studentDto the StudentDto object to map
     * @return the mapped Student object
     */
    public static Student mapDtoToStudent(StudentDto studentDto) {
        return MODEL_MAPPER.map(studentDto, Student.class);
    }

}