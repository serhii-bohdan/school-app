package ua.foxminded.schoolapp.cli.impl;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Comparator;
import ua.foxminded.schoolapp.cli.SchoolView;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

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
        System.out.print("""
                            **************************
                            -----   SCHOOL APP   -----
                            **************************
                1. Find all groups with less or equal students’ number.
                2. Find all students related to the course with the given name.
                3. Add a new student.
                4. Delete a student.
                5. Add a student to the course.
                6. Remove the student from one of their courses.

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
        System.out.print(message);
        return getIntInput();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWordFromUser(String message) {
        System.out.print(message);
        return scanner.next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfirmationFromUserAboutDeletingStudent(Student student) {
        String confirmationQuestion = String.format("Are you sure you want to delete a student %s %s?",
                student.getFirstName(), student.getLastName());
        System.out.print(
                confirmationQuestion + NEW_LINE + "Please confirm your actions (enter Y or N):" + NON_BREAKING_SPACE);
        return scanner.next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayGroupsWithTheirNumberOfStudents(Map<Group, Integer> groupsWithTheirNumberOfStudents) {
        StringBuilder formattedGroups = new StringBuilder("Groups with their number of students:");
        String lineBetweenRows = "";

        for (Map.Entry<Group, Integer> entry : groupsWithTheirNumberOfStudents.entrySet()) {
            Group group = entry.getKey();
            String tableRow = String.format("| %s | %d |", group.getGroupName(), entry.getValue());
            String lineBetweenRowsWithoutPluses = makeCharacterSequence(tableRow.length(), '-');
            lineBetweenRows = formatLineWithPluses(lineBetweenRowsWithoutPluses, 3 + group.getGroupName().length());
            tableRow = String.format(NEW_LINE + "%-23s%s", "", tableRow);
            lineBetweenRows = String.format(NEW_LINE + "%-23s%s", "", lineBetweenRows);
            formattedGroups.append(tableRow);
            formattedGroups.append(lineBetweenRows);
        }

        formattedGroups.insert(37, lineBetweenRows);
        System.out.print(formattedGroups + NEW_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayStudentsWithTheirCourses(Map<Student, List<Course>> studentsWithTheirCourses) {
        StringBuilder formattedStudents = new StringBuilder("Students with their courses:");
        List<String> studentsFullNames = new ArrayList<>();
        List<String> coursesNamesEnumerationForEachStudent = new ArrayList<>();
        String lineBetweenRows = "";

        for (Map.Entry<Student, List<Course>> entry : studentsWithTheirCourses.entrySet()) {
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
        System.out.print(formattedStudents + NEW_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayCourses(List<Course> courses) {
        StringBuilder formattedCourses = new StringBuilder("Courses with descriptions:");
        String lineBetweenRows = "";
        String maxCourseName = getMaxCousrse("Name", courses);
        String maxCourseDescription = getMaxCousrse("Description", courses);

        for (Course course : courses) {
            String spacesAfterCourseName = makeCharacterSequence(
                    maxCourseName.length() - course.getCourseName().length(), ' ');
            String spacesAfterCourseDescription = makeCharacterSequence(
                    maxCourseDescription.length() - course.getDescription().length(), ' ');
            String tableRow = String.format(NEW_LINE + "| %s%s | %s%s |", course.getCourseName(), spacesAfterCourseName,
                    course.getDescription(), spacesAfterCourseDescription);
            String lineBetweenRowsWithoutPluses = makeCharacterSequence(tableRow.length() - 1, '-');
            lineBetweenRows = NEW_LINE + formatLineWithPluses(lineBetweenRowsWithoutPluses, 3 + maxCourseName.length());
            formattedCourses.append(tableRow);
            formattedCourses.append(lineBetweenRows);
        }

        formattedCourses.insert(26, lineBetweenRows);
        System.out.print(formattedCourses);
    }

    private int getIntInput() {
        while (true) {

            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.print("Invalid input. Please enter a valid integer value: ");
                scanner.next();
            }
        }
    }

    private String makeCharacterSequence(Integer count, Character character) {
        return String.valueOf(character).repeat(Math.max(0, count));
    }

    private String getStudentFullName(Student student) {
        return student.getFirstName() + " " + student.getLastName();
    }

    private String getCoursesEnumeration(List<Course> coursesForStudent) {
        return coursesForStudent.stream()
                .map(Course::getCourseName)
                .collect(Collectors.joining(", "));
    }

    private String getStringWithMaxLength(List<String> stringsList) {
        return stringsList.stream()
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }

    private String getMaxCousrse(String identifier, List<Course> courses) {
        Stream<String> coursesNamesOrDescriptions = null;

        if ("Name".equals(identifier)) {
            coursesNamesOrDescriptions = courses.stream()
                    .map(Course::getCourseName);
        } else if ("Description".equals(identifier)) {
            coursesNamesOrDescriptions = courses.stream()
                    .map(Course::getDescription);
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
