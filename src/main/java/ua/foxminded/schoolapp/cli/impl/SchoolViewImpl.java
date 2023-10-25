package ua.foxminded.schoolapp.cli.impl;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Comparator;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.cli.SchoolView;

/**
 * The SchoolViewImpl class implements the {@link SchoolView} interface and
 * provides the functionality to display information and interact with the user
 * through the console in the school application.
 * <p>
 * The class is annotated with {@code @Component} to indicate that it is a
 * Spring component, and it can be automatically discovered and registered as a
 * bean in the Spring context. The SchoolViewImpl requires an instance of
 * {@link Scanner} for user input and display of information.
 * <p>
 * The class contains various methods to display menus, messages, and
 * information related to groups, students, and courses. It provides methods to
 * interact with the user and receive input from the console.
 *
 * @author Serhii Bohdan
 */
@Component
public class SchoolViewImpl implements SchoolView {

    /**
     * A constant representing a new line character.
     */
    private static final String NEW_LINE = "\n";

    /**
     * A constant representing a non-breaking space character.
     */
    private static final String NON_BREAKING_SPACE = "\u00A0";

    private final Scanner scanner;

    /**
     * Constructs a new SchoolView with the specified scanner.
     *
     * @param scanner the scanner to be used for user input
     */
    public SchoolViewImpl(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMenu() {
        printMessage("""
                             **************************
                             -----   SCHOOL APP   -----
                             **************************
                 1. Find all groups with less or equal students’ number.
                 2. Find all students related to the course with the given name.
                 3. Add a student to the course.
                 4. Remove the student from one of their courses.
                 5. Add a new group.
                 6. Update group information.
                 7. Delete a group.
                 8. Add a new student.
                 9. Update student information.
                10. Delete a student.
                11. Add a new course.
                12. Update course information.
                13. Delete a course.

                Enter 0 to exit the program.
                """);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printMessage(String message) {
        System.out.print(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntNumberFromUser(String message) {
        printMessage(message);
        return getIntInput();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSentenceFromUser(String message) {
        printMessage(message);
        return scanner.nextLine();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfirmationAboutDeletingStudent(StudentDto student) {
        String confirmationQuestion = String.format("Are you sure you want to delete a student %s %s?",
                student.getFirstName(), student.getLastName());

        return getConfirmationAnswer(confirmationQuestion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfirmationAboutDeletingGroup(GroupDto group) {
        String confirmationQuestion = String.format("Are you sure you want to delete a group %s?\n"
                + "WARNING: Students who belong to this group will also be deleted.", group.getGroupName());

        return getConfirmationAnswer(confirmationQuestion);
    }

    /**
     * {@inheritDoc}
     */
    public String getConfirmationAboutDeletingCourse(CourseDto course) {
        String confirmationQuestion = String.format("Are you sure you want to delete a course %s?",
                course.getCourseName());

        return getConfirmationAnswer(confirmationQuestion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayGroups(List<GroupDto> groups) {
        StringBuilder formattedGroups = new StringBuilder();
        String lineBetweenRows = "";

        for (GroupDto group : groups) {
            String tableRow = String.format("| %s |", group.getGroupName());
            String lineBetweenRowsWithoutPluses = makeCharacterSequence(tableRow.length(), '-');
            lineBetweenRows = formatLineWithPluses(lineBetweenRowsWithoutPluses, 0);
            tableRow = String.format(NEW_LINE + "%-23s%s", "", tableRow);
            lineBetweenRows = String.format(NEW_LINE + "%-23s%s", "", lineBetweenRows);
            formattedGroups.append(tableRow);
            formattedGroups.append(lineBetweenRows);
        }

        formattedGroups.insert(0, lineBetweenRows);
        printMessage(formattedGroups + NEW_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayGroupsWithTheirNumberOfStudents(Map<GroupDto, Integer> groupsWithTheirNumberOfStudents) {
        StringBuilder formattedGroups = new StringBuilder("Groups with their number of students:");
        List<String> groupNames = new ArrayList<>();
        List<String> numberOfStudentsInGroups = new ArrayList<>();
        String lineBetweenRows = "";

        for (Map.Entry<GroupDto, Integer> entry : groupsWithTheirNumberOfStudents.entrySet()) {
            groupNames.add(entry.getKey().getGroupName());
            numberOfStudentsInGroups.add(entry.getValue().toString());
        }

        String maxStudentsNumber = getStringWithMaxLength(numberOfStudentsInGroups);

        for (int i = 0; i < groupNames.size(); i++) {
            String groupName = groupNames.get(i);
            String studentsNumber = numberOfStudentsInGroups.get(i);
            String spacesAfterStudentsNumber = makeCharacterSequence(
                    maxStudentsNumber.length() - studentsNumber.length(), ' ');
            String tableRow = String.format("| %s | %s%s |", groupName, spacesAfterStudentsNumber, studentsNumber);
            String lineBetweenRowsWithoutPluses = makeCharacterSequence(tableRow.length(), '-');
            lineBetweenRows = formatLineWithPluses(lineBetweenRowsWithoutPluses, 3 + groupName.length());
            tableRow = String.format(NEW_LINE + "%-23s%s", "", tableRow);
            lineBetweenRows = String.format(NEW_LINE + "%-23s%s", "", lineBetweenRows);
            formattedGroups.append(tableRow);
            formattedGroups.append(lineBetweenRows);
        }

        formattedGroups.insert(37, lineBetweenRows);
        printMessage(formattedGroups + NEW_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayStudentsWithTheirGroups(Map<StudentDto, GroupDto> studentsWithTheirGroups) {
        StringBuilder formattedStudentsWithGroups = new StringBuilder();
        List<String> studentsFullNames = new ArrayList<>();
        List<String> groupNames = new ArrayList<>();
        String lineBetweenRows = "";

        for (Map.Entry<StudentDto, GroupDto> entry : studentsWithTheirGroups.entrySet()) {
            studentsFullNames.add(getStudentFullName(entry.getKey()));
            groupNames.add(entry.getValue().getGroupName());
        }

        String maxStudentFullName = getStringWithMaxLength(studentsFullNames);

        for (int i = 0; i < studentsFullNames.size(); i++) {
            String studentFullName = studentsFullNames.get(i);
            String groupName = groupNames.get(i);
            String spacesAfterStudentFullName = makeCharacterSequence(
                    maxStudentFullName.length() - studentFullName.length(), ' ');
            String tableRow = String.format("| %s%s | %s |", studentFullName, spacesAfterStudentFullName, groupName);
            String lineBetweenRowsWithoutPluses = makeCharacterSequence(tableRow.length(), '-');
            lineBetweenRows = formatLineWithPluses(lineBetweenRowsWithoutPluses, 3 + maxStudentFullName.length());
            tableRow = String.format(NEW_LINE + "%-18s%s", "", tableRow);
            lineBetweenRows = String.format(NEW_LINE + "%-18s%s", "", lineBetweenRows);
            formattedStudentsWithGroups.append(tableRow);
            formattedStudentsWithGroups.append(lineBetweenRows);
        }

        formattedStudentsWithGroups.insert(0, lineBetweenRows);
        printMessage(formattedStudentsWithGroups + NEW_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayStudentsWithTheirCourses(Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses) {
        StringBuilder formattedStudents = new StringBuilder("Students with their courses:");
        List<String> studentsFullNames = new ArrayList<>();
        List<String> coursesNamesEnumerationForEachStudent = new ArrayList<>();
        String lineBetweenRows = "";

        for (Map.Entry<StudentDto, Set<CourseDto>> entry : studentsWithTheirCourses.entrySet()) {
            studentsFullNames.add(getStudentFullName(entry.getKey()));
            coursesNamesEnumerationForEachStudent.add(getCoursesEnumeration(entry.getValue()));
        }

        String maxStudentFullName = getStringWithMaxLength(studentsFullNames);
        String maxCoursesNamesEnumeration = getStringWithMaxLength(coursesNamesEnumerationForEachStudent);

        for (int i = 0; i < studentsFullNames.size(); i++) {
            String studentFullName = studentsFullNames.get(i);
            String coursesNamesEnumeration = coursesNamesEnumerationForEachStudent.get(i);
            String spacesAfterStudentFullName = makeCharacterSequence(
                    maxStudentFullName.length() - studentFullName.length(), ' ');
            String spacesAfterCoursesNamesEnumeration = makeCharacterSequence(
                    maxCoursesNamesEnumeration.length() - coursesNamesEnumeration.length(), ' ');
            String tableRow = String.format(NEW_LINE + "| %s%s | %s%s |", studentFullName, spacesAfterStudentFullName,
                    coursesNamesEnumeration, spacesAfterCoursesNamesEnumeration);
            String lineBetweenRowsWithoutPluses = makeCharacterSequence(tableRow.length() - 1, '-');
            lineBetweenRows = NEW_LINE
                    + formatLineWithPluses(lineBetweenRowsWithoutPluses, 3 + maxStudentFullName.length());
            formattedStudents.append(tableRow);
            formattedStudents.append(lineBetweenRows);
        }

        formattedStudents.insert(28, lineBetweenRows);
        printMessage(formattedStudents + NEW_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayCourses(List<CourseDto> coursesDtos) {
        StringBuilder formattedCourses = new StringBuilder();
        String lineBetweenRows = "";
        String maxCourseName = getMaxCousrse("Name", coursesDtos);
        String maxCourseDescription = getMaxCousrse("Description", coursesDtos);

        for (CourseDto courseDto : coursesDtos) {
            String spacesAfterCourseName = makeCharacterSequence(
                    maxCourseName.length() - courseDto.getCourseName().length(), ' ');
            String spacesAfterCourseDescription = makeCharacterSequence(
                    maxCourseDescription.length() - courseDto.getDescription().length(), ' ');
            String tableRow = String.format(NEW_LINE + "| %s%s | %s%s |", courseDto.getCourseName(),
                    spacesAfterCourseName, courseDto.getDescription(), spacesAfterCourseDescription);
            String lineBetweenRowsWithoutPluses = makeCharacterSequence(tableRow.length() - 1, '-');
            lineBetweenRows = NEW_LINE + formatLineWithPluses(lineBetweenRowsWithoutPluses, 3 + maxCourseName.length());
            formattedCourses.append(tableRow);
            formattedCourses.append(lineBetweenRows);
        }

        formattedCourses.insert(0, lineBetweenRows);
        printMessage(formattedCourses + NEW_LINE);
    }

    private int getIntInput() {
        try {

            while (true) {
                try {
                    return Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    printMessage("Invalid input. Please enter a valid integer value: ");
                }
            }

        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    private String getConfirmationAnswer(String confirmationQuestion) {
        printMessage(confirmationQuestion + NEW_LINE + "Please confirm your actions (enter Y or N):" + NON_BREAKING_SPACE);
        return scanner.nextLine();
    }

    private String makeCharacterSequence(Integer count, Character character) {
        return String.valueOf(character).repeat(Math.max(0, count));
    }

    private String getStudentFullName(StudentDto studentDto) {
        return studentDto.getFirstName() + " " + studentDto.getLastName();
    }

    private String getCoursesEnumeration(Set<CourseDto> coursesForStudent) {
        return coursesForStudent.stream()
                .map(CourseDto::getCourseName)
                .collect(Collectors.joining(", "));
    }

    private String getStringWithMaxLength(List<String> stringsList) {
        return stringsList.stream()
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }

    private String getMaxCousrse(String identifier, List<CourseDto> coursesDtos) {
        Stream<String> coursesNamesOrDescriptions = null;

        if ("Name".equals(identifier)) {
            coursesNamesOrDescriptions = coursesDtos.stream()
                    .map(CourseDto::getCourseName);
        } else if ("Description".equals(identifier)) {
            coursesNamesOrDescriptions = coursesDtos.stream()
                    .map(CourseDto::getDescription);
        } else {
            throw new RuntimeException("An instance of the Course type does not contain this field: " + identifier);
        }

        return getStringWithMaxLength(coursesNamesOrDescriptions.toList());
    }

    private String formatLineWithPluses(String line, Integer plusIndexInsideLine) {
        StringBuilder lineWithPluses = new StringBuilder(line);
        lineWithPluses.setCharAt(0, '+');
        lineWithPluses.setCharAt(plusIndexInsideLine, '+');
        lineWithPluses.setCharAt(line.length() - 1, '+');
        return lineWithPluses.toString();
    }

}
