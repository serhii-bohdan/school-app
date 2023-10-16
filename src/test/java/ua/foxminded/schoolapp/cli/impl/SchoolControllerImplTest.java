package ua.foxminded.schoolapp.cli.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.cli.SchoolView;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.service.logic.ServiceFacade;

@SpringBootTest(classes = { SchoolControllerImpl.class })
class SchoolControllerImplTest {

    static final String NEW_LINE = "\n";
    static final String NON_BREAKING_SPACE = "\u00A0";
    static final String SELECTION = "Select an option: ";

    @MockBean
    SchoolView viewMock;

    @MockBean
    ServiceFacade serviceFacadeMock;

    @Autowired
    SchoolControllerImpl controller;

    @Test
    void runSchoolApp_shouldPrintedMessageAboutIncorrectnessEnteredNumber_whenSelectedFirstOptionAndMapWithGroupsAndTheirNumberOfStudentsIsNull() {
        Integer numberGreaterThanMaximumStudentsCountInGroup = -10;
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(1, 0);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Enter the number of students:" + NON_BREAKING_SPACE))
                .thenReturn(numberGreaterThanMaximumStudentsCountInGroup);
        when(serviceFacadeMock.getGroupsWithGivenNumberOfStudents(numberGreaterThanMaximumStudentsCountInGroup))
                .thenReturn(null);

        controller.runSchoolApp();

        verify(viewMock)
                .printMessage(NEW_LINE + "You want to know groups with a given and smaller number of students.");
        verify(viewMock).printMessage("""
                The entered number of students is not correct.
                The number must be greater than or equal to zero.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatGroupsListEmpty_whenSelectedFirstOptionAndMapWithGroupsAndTheirNumberOfStudentsIsEmpty() {
        Integer verySmallStudentsNumber = 10;
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(1, 0);
        when(viewMock.getIntNumberFromUser("Enter the number of students:" + NON_BREAKING_SPACE))
                .thenReturn(verySmallStudentsNumber);
        when(serviceFacadeMock.getGroupsWithGivenNumberOfStudents(verySmallStudentsNumber)).thenReturn(new HashMap<>());

        controller.runSchoolApp();

        verify(viewMock)
                .printMessage(NEW_LINE + "You want to know groups with a given and smaller number of students.");
        verify(viewMock).printMessage("The list of groups is empty." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldDisplayedGroupsNamesWithTheirNumberOfStudents_whenSelectedFirstOptionAndMapWithGroupsAndTheirNumberOfStudentsContainsSomeGroups() {
        Integer studentsNumber = 18;
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        groupsWithTheirNumberOfStudents.put(new GroupDto("FG-62"), 17);
        groupsWithTheirNumberOfStudents.put(new GroupDto("LK-56"), 15);
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(1, 0);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Enter the number of students:" + NON_BREAKING_SPACE))
                .thenReturn(studentsNumber);
        when(serviceFacadeMock.getGroupsWithGivenNumberOfStudents(studentsNumber))
                .thenReturn(groupsWithTheirNumberOfStudents);

        controller.runSchoolApp();

        verify(viewMock)
                .printMessage(NEW_LINE + "You want to know groups with a given and smaller number of students.");
        verify(viewMock).displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatCourseNotExist_whenSelectedSecondOptionAndEnteredCourseNameNotExist() {
        String nonExistentCourseName = "NonExistentCourseName";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(2, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter the name of the course:" + NON_BREAKING_SPACE))
                .thenReturn(nonExistentCourseName);
        when(serviceFacadeMock.getStudentsWithCoursesByCourseName(nonExistentCourseName)).thenReturn(null);

        controller.runSchoolApp();

        verify(viewMock).printMessage(
                NEW_LINE + "You want to know the list of students related to the course. All available courses:");
        verify(viewMock).printMessage("A course with that name does not exist." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentsListEmpty_whenSelectedSecondOptionAndMapWithStudentsAndTheirCoursesEmpty() {
        String existentCourseName = "CourseName_1";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(2, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter the name of the course:" + NON_BREAKING_SPACE))
                .thenReturn(existentCourseName);
        when(serviceFacadeMock.getStudentsWithCoursesByCourseName(existentCourseName)).thenReturn(new HashMap<>());

        controller.runSchoolApp();

        verify(viewMock).printMessage(
                NEW_LINE + "You want to know the list of students related to the course. All available courses:");
        verify(viewMock).printMessage("The list of students is empty." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldDisplayedStudentsNamesWithTheirCourses_whenSelectedSecondOptionAndEnteredExistentCourseName() {
        String existentCourseName = "CourseName_1";
        Map<StudentDto, Set<CourseDto>> studentWithTheirCourses = new HashMap<>();
        StudentDto student = new StudentDto("FirstName_1", "LastName_1");
        CourseDto firstCourse = new CourseDto("CourseName_1", "Description_1");
        CourseDto secondCourse = new CourseDto("CourseName_2", "Description_2");
        Set<CourseDto> coursesForStudent = new HashSet<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        studentWithTheirCourses.put(student, coursesForStudent);
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(2, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter the name of the course:" + NON_BREAKING_SPACE))
                .thenReturn(existentCourseName);
        when(serviceFacadeMock.getStudentsWithCoursesByCourseName(existentCourseName))
                .thenReturn(studentWithTheirCourses);

        controller.runSchoolApp();

        verify(viewMock).printMessage(
                NEW_LINE + "You want to know the list of students related to the course. All available courses:");
        verify(viewMock).displayStudentsWithTheirCourses(studentWithTheirCourses);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentSuccessfullyAddedToCourse_whenSelectedThirdOptionAndStudentSuccessfullyAddedToCourse() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(3, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getSentenceFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getSentenceFromUser("Enter the name of the course:" + NON_BREAKING_SPACE)).thenReturn(courseName);
        when(serviceFacadeMock.addStudentToCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a student (from the list) to the course." + NEW_LINE);
        verify(viewMock).printMessage("The student has been successfully added to the course." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotAddedToCourse_whenSelectedThirdOptionAndStudentAlreadyAttendingCourseOrEnteredIncorrectStudentName() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(3, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getSentenceFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getSentenceFromUser("Enter the name of the course:" + NON_BREAKING_SPACE)).thenReturn(courseName);
        when(serviceFacadeMock.addStudentToCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a student (from the list) to the course." + NEW_LINE);
        verify(viewMock).printMessage("""
                The student has not been added to the course. Possible causes of failure:
                1. Invalid student name entered.
                2. The course name you entered is invalid.
                3. The student is already registered for the course.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentSuccessfullyDeletedFromCourse_whenSelectedFourthOptionAndStudentSuccessfullyDeletedFromCourse() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(4, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getSentenceFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getSentenceFromUser("Enter the name of the course:" + NON_BREAKING_SPACE)).thenReturn(courseName);
        when(serviceFacadeMock.deleteStudentFromCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student from a course." + NEW_LINE);
        verify(viewMock).printMessage("The student has been successfully deleted from the course." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotDeletedFromCourse_whenSelectedFourthOptionAndStudentNotPreviouslyAttendedCourseOrEnteredIncorrectStudentName() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(4, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getSentenceFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getSentenceFromUser("Enter the name of the course:" + NON_BREAKING_SPACE)).thenReturn(courseName);
        when(serviceFacadeMock.addStudentToCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student from a course." + NEW_LINE);
        verify(viewMock).printMessage("""
                The student was not deleted from the course. Possible causes of failure:
                1. Invalid student name entered.
                2. The course name you entered is invalid.
                3. The student is not registered on the course.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatGroupWasSuccessfullyAdded_whenSelectedFifthOptionAndGroupWithEnteredNameSuccessfullAdded() {
        String groupName = "LF-19";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(5, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter a name for the new group:" + NON_BREAKING_SPACE))
                .thenReturn(groupName);
        when(serviceFacadeMock.addNewGroup(groupName)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a new group. All available groups:");
        verify(viewMock).printMessage("New group added successfully." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatGroupWasNotAdded_whenSelectedFifthOptionAndGroupWithEnteredNameNotAdded() {
        String groupName = "NotExistent";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(5, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter a name for the new group:" + NON_BREAKING_SPACE))
                .thenReturn(groupName);
        when(serviceFacadeMock.addNewGroup(groupName)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a new group. All available groups:");
        verify(viewMock).printMessage("""
                No new group has been added. Possible causes of failure:
                1. The group name does not match the pattern.
                2. A group with this name already exists.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatGroupWasSuccessfullyUpdated_whenSelectedSixthOptionAndGroupInformationUpdated() {
        String groupNameToUpdate = "LF-19";
        String newGroupName = "FD-21";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(6, 0);
        when(viewMock
                .getSentenceFromUser(NEW_LINE + "Enter the name of the group you want to update:" + NON_BREAKING_SPACE))
                .thenReturn(groupNameToUpdate);
        when(viewMock.getSentenceFromUser("Enter a new group name:" + NON_BREAKING_SPACE)).thenReturn(newGroupName);
        when(serviceFacadeMock.updateGroup(groupNameToUpdate, newGroupName)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to update the group information. All available groups:");
        verify(viewMock).printMessage("The group information was successfully updated." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatGroupInformationNotUpdated_whenSelectedSixthOptionAndGroupInformationNotUpdated() {
        String groupNameToUpdate = "LF-19";
        String newGroupName = "NotMatchesPattern";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(6, 0);
        when(viewMock
                .getSentenceFromUser(NEW_LINE + "Enter the name of the group you want to update:" + NON_BREAKING_SPACE))
                .thenReturn(groupNameToUpdate);
        when(viewMock.getSentenceFromUser("Enter a new group name:" + NON_BREAKING_SPACE)).thenReturn(newGroupName);
        when(serviceFacadeMock.updateGroup(groupNameToUpdate, newGroupName)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to update the group information. All available groups:");
        verify(viewMock).printMessage("""
                The group information has not been updated. Possible causes of failure:
                1. The name of the group you want to update is invalid.
                2. A group with the new name already exists.
                3. The new name does not match the pattern.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatGroupDeleted_whenSelectedSeventhOptionAndGroupWithGivenNameExistAndUserConfirmedDeletion() {
        String groupNameToDelete = "LF-19";
        GroupDto group = new GroupDto(groupNameToDelete);
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(7, 0);
        when(viewMock
                .getSentenceFromUser(NEW_LINE + "Enter the name of the group you want to delete:" + NON_BREAKING_SPACE))
                .thenReturn(groupNameToDelete);
        when(serviceFacadeMock.getGroupByName(groupNameToDelete)).thenReturn(group);
        when(viewMock.getConfirmationAboutDeletingGroup(group)).thenReturn("Y");

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a group by its name. All available groups:");
        verify(serviceFacadeMock).deleteGroupByName(groupNameToDelete);
        verify(viewMock).printMessage("The group was successfully deleted." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatGroupNotDeleted_whenSelectedSeventhOptionAndGroupWithGivenNameExistAndUserNotConfirmedDeletion() {
        String groupNameToDelete = "LF-19";
        GroupDto group = new GroupDto(groupNameToDelete);
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(7, 0);
        when(viewMock
                .getSentenceFromUser(NEW_LINE + "Enter the name of the group you want to delete:" + NON_BREAKING_SPACE))
                .thenReturn(groupNameToDelete);
        when(serviceFacadeMock.getGroupByName(groupNameToDelete)).thenReturn(group);
        when(viewMock.getConfirmationAboutDeletingGroup(group)).thenReturn("N");

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a group by its name. All available groups:");
        verify(viewMock).printMessage("The group was not deleted." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatEnteredNotExistentOption_whenSelectedSeventhOptionAndGroupWithGivenNameExistAndUserEnterNonExistentOption() {
        String groupNameToDelete = "LF-19";
        GroupDto group = new GroupDto(groupNameToDelete);
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(7, 0);
        when(viewMock
                .getSentenceFromUser(NEW_LINE + "Enter the name of the group you want to delete:" + NON_BREAKING_SPACE))
                .thenReturn(groupNameToDelete);
        when(serviceFacadeMock.getGroupByName(groupNameToDelete)).thenReturn(group);
        when(viewMock.getConfirmationAboutDeletingGroup(group)).thenReturn("OptionNotExist");

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a group by its name. All available groups:");
        verify(viewMock).printMessage("There is no such option." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoGroupWithGivenName_whenSelectedSeventhOptionAndGroupWithGivenNameNotExist() {
        String groupNameToDelete = "NotExistent";
        GroupDto group = null;
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(7, 0);
        when(viewMock
                .getSentenceFromUser(NEW_LINE + "Enter the name of the group you want to delete:" + NON_BREAKING_SPACE))
                .thenReturn(groupNameToDelete);
        when(serviceFacadeMock.getGroupByName(groupNameToDelete)).thenReturn(group);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a group by its name. All available groups:");
        verify(viewMock).printMessage("There is no group with this name." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentWasSuccessfullyAdded_whenSelectedEighthOptionAndStudentWithEnteredDataSuccessfullAdded() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String groupName = "LS-32";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(8, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getSentenceFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getSentenceFromUser(
                "Enter the name of the group to which the student should belong:" + NON_BREAKING_SPACE))
                .thenReturn(groupName);
        when(serviceFacadeMock.addNewStudent(studentFirstName, studentLastName, groupName)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a new student. All available groups:");
        verify(viewMock).printMessage("The student has been successfully added." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotAdded_whenSelectedEighthOptionAndStudentWithEnteredDataAlreadyPresent() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String groupName = "LS-32";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(8, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getSentenceFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getSentenceFromUser(
                "Enter the name of the group to which the student should belong:" + NON_BREAKING_SPACE))
                .thenReturn(groupName);
        when(serviceFacadeMock.addNewStudent(studentFirstName, studentLastName, groupName)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a new student. All available groups:");
        verify(viewMock).printMessage("""
                No new student was added. Possible causes of failure:
                1. A student with the entered name already exists.
                2. The student's first or last name contains more than 25 characters.
                3. There is no group with the entered name.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentInformationWasUpdated_whenSelectedNinthOptionAndStudentUpdated() {
        String studentFirstNameToUpdate = "OldFirstName";
        String studentLastNameToUpdate = "OldLastName";
        String newStudentFirstName = "NewFirstName";
        String newStudentLastName = "NewLastName";
        String newGroupName = "GH-23";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(9, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE
                + "Enter the fitst name of the student whose information you want to update:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstNameToUpdate);
        when(viewMock.getSentenceFromUser(
                "Enter the last name of the student whose information you want to update:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastNameToUpdate);
        when(viewMock.getSentenceFromUser("Enter new student first name:" + NON_BREAKING_SPACE))
                .thenReturn(newStudentFirstName);
        when(viewMock.getSentenceFromUser("Enter new student last name:" + NON_BREAKING_SPACE))
                .thenReturn(newStudentLastName);
        when(viewMock.getSentenceFromUser("Enter a new group name:" + NON_BREAKING_SPACE)).thenReturn(newGroupName);
        when(serviceFacadeMock.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate, newStudentFirstName,
                newStudentLastName, newGroupName)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(
                NEW_LINE + "You want to update the student information. All available students with their groups:");
        verify(viewMock).printMessage("Student information has been successfully updated." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentInformationWasNotUpdated_whenSelectedNinthOptionAndStudentNotUpdated() {
        String studentFirstNameToUpdate = "OldFirstName";
        String studentLastNameToUpdate = "OldLastName";
        String newStudentFirstName = "NewFirstName";
        String newStudentLastName = "NewLastName";
        String newGroupName = "GH-23";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(9, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE
                + "Enter the fitst name of the student whose information you want to update:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstNameToUpdate);
        when(viewMock.getSentenceFromUser(
                "Enter the last name of the student whose information you want to update:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastNameToUpdate);
        when(viewMock.getSentenceFromUser("Enter new student first name:" + NON_BREAKING_SPACE))
                .thenReturn(newStudentFirstName);
        when(viewMock.getSentenceFromUser("Enter new student last name:" + NON_BREAKING_SPACE))
                .thenReturn(newStudentLastName);
        when(viewMock.getSentenceFromUser("Enter a new group name:" + NON_BREAKING_SPACE)).thenReturn(newGroupName);
        when(serviceFacadeMock.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate, newStudentFirstName,
                newStudentLastName, newGroupName)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(
                NEW_LINE + "You want to update the student information. All available students with their groups:");
        verify(viewMock).printMessage("""
                Student information has not been updated. Possible causes of failure:
                1. The student whose data you want to update does not exist.
                2. The updated full name belongs to another student.
                3. The updated student's first or last name contains more than 25 characters.
                4. There is no group with the entered name.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldDeletedStudentFromDatabase_whenSelectedTenOptionAndStudentWithEnteredIdExistAndUserConfirmedDeletion() {
        Integer studentId = 1;
        StudentDto student = new StudentDto("FirstName", "LastName");
        String сonfirmationToDeleteStudent = "Y";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(10, 0);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Enter your student ID:" + NON_BREAKING_SPACE))
                .thenReturn(studentId);
        when(serviceFacadeMock.getStudentById(studentId)).thenReturn(student);
        when(viewMock.getConfirmationAboutDeletingStudent(student)).thenReturn(сonfirmationToDeleteStudent);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student by their ID.");
        verify(serviceFacadeMock).deleteStudentById(studentId);
        verify(viewMock).printMessage("The student was successfully deleted." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotDeletedFromDatabase_whenSelectedTenOptionAndStudentWithEnteredIdExistsAndUserNotConfirmedDeletion() {
        Integer studentId = 1;
        StudentDto student = new StudentDto("FirstName", "LastName");
        String disagreementToDeleteStudent = "N";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(10, 0);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Enter your student ID:" + NON_BREAKING_SPACE))
                .thenReturn(studentId);
        when(serviceFacadeMock.getStudentById(studentId)).thenReturn(student);
        when(viewMock.getConfirmationAboutDeletingStudent(student)).thenReturn(disagreementToDeleteStudent);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student by their ID.");
        verify(viewMock).printMessage("The student was not deleted." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoEnteredOption_whenSelectedTenOptionAndStudentWithEnteredIdExistsAndUserEnterNonExistentOption() {
        Integer studentId = 1;
        StudentDto student = new StudentDto("FirstName", "LastName");
        String nonExistentOption = "NonExistentOption";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(10, 0);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Enter your student ID:" + NON_BREAKING_SPACE))
                .thenReturn(studentId);
        when(serviceFacadeMock.getStudentById(studentId)).thenReturn(student);
        when(viewMock.getConfirmationAboutDeletingStudent(student)).thenReturn(nonExistentOption);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student by their ID.");
        verify(viewMock).printMessage("There is no such option." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoExistStudentWithEnteredId_whenSelectedTenOptionAndNoStudentWithEnteredId() {
        int studentId = 1;
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(10, 0);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Enter your student ID:" + NON_BREAKING_SPACE))
                .thenReturn(studentId);
        when(serviceFacadeMock.getStudentById(studentId)).thenReturn(null);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student by their ID.");
        verify(viewMock).printMessage("There is no student with this ID." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatCourseWasAdded_whenSelectedEleventhOptionAndCourseWithEnteredDataSuccessfullyAdded() {
        String courseName = "CourseName";
        String description = "Description";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(11, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter a name for the new course:" + NON_BREAKING_SPACE))
                .thenReturn(courseName);
        when(viewMock.getSentenceFromUser("Enter a description for the new course:" + NON_BREAKING_SPACE))
                .thenReturn(description);
        when(serviceFacadeMock.addNewCourse(courseName, description)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a new course. All available courses:");
        verify(viewMock).printMessage("New course added successfully." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatCourseWasNotAdded_whenSelectedEleventhOptionAndCourseWithEnteredDataNotAdded() {
        String courseName = "CourseName";
        String description = "Description";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(11, 0);
        when(viewMock.getSentenceFromUser(NEW_LINE + "Enter a name for the new course:" + NON_BREAKING_SPACE))
                .thenReturn(courseName);
        when(viewMock.getSentenceFromUser("Enter a description for the new course:" + NON_BREAKING_SPACE))
                .thenReturn(description);
        when(serviceFacadeMock.addNewCourse(courseName, description)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a new course. All available courses:");
        verify(viewMock).printMessage("""
                No new course was added. Possible causes of failure:
                1. A course with this name or description may already exist.
                2. The course name contains more than 25 characters.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatCourseWasUpdated_whenSelectedTwelfthOptionAndCourseSuccessfullyUpdated() {
        String courseNameToUpdate = "OldCourseName";
        String newCourseName = "NewCourseName";
        String newDescription = "NewDescription";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(12, 0);
        when(viewMock.getSentenceFromUser(
                NEW_LINE + "Enter the name of the course you want to update:" + NON_BREAKING_SPACE))
                .thenReturn(courseNameToUpdate);
        when(viewMock.getSentenceFromUser("Enter a new course name:" + NON_BREAKING_SPACE)).thenReturn(newCourseName);
        when(viewMock.getSentenceFromUser("Enter a new course description:" + NON_BREAKING_SPACE))
                .thenReturn(newDescription);
        when(serviceFacadeMock.updateCourse(courseNameToUpdate, newCourseName, newDescription)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to update the course information. All available courses:");
        verify(viewMock).printMessage("Course information has been successfully updated." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatCourseNotUpdated_whenSelectedTwelfthOptionAndCourseNotUpdated() {
        String courseNameToUpdate = "NotExistent";
        String newCourseName = "NewCourseName";
        String newDescription = "NewDescription";
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(12, 0);
        when(viewMock.getSentenceFromUser(
                NEW_LINE + "Enter the name of the course you want to update:" + NON_BREAKING_SPACE))
                .thenReturn(courseNameToUpdate);
        when(viewMock.getSentenceFromUser("Enter a new course name:" + NON_BREAKING_SPACE)).thenReturn(newCourseName);
        when(viewMock.getSentenceFromUser("Enter a new course description:" + NON_BREAKING_SPACE))
                .thenReturn(newDescription);
        when(serviceFacadeMock.updateCourse(courseNameToUpdate, newCourseName, newDescription)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to update the course information. All available courses:");
        verify(viewMock).printMessage("""
                Course information has not been updated. Possible causes of failure:
                1. You have entered a course name that does not exist.
                2. The updated course name contains more than 25 characters.
                3. The updated course name or description belong to another course.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldDeletedCourse_whenSelectedThirteenthOptionAndCourseWithGivenNameExistAndUserConfirmedDeletion() {
        String courseName = "CourseName";
        CourseDto course = new CourseDto(courseName, "Description");
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(13, 0);
        when(viewMock.getSentenceFromUser(
                NEW_LINE + "Enter the name of the course you want to delete:" + NON_BREAKING_SPACE))
                .thenReturn(courseName);
        when(serviceFacadeMock.getCourseByName(courseName)).thenReturn(course);
        when(viewMock.getConfirmationAboutDeletingCourse(course)).thenReturn("Y");

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete the course by its name. All available courses:");
        verify(serviceFacadeMock).deleteCourseByName(courseName);
        verify(viewMock).printMessage("The course was successfully deleted." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldNotDeletedCourse_whenSelectedThirteenthOptionAndCourseWithGivenNameExistAndUserNotConfirmedDeletion() {
        String courseName = "CourseName";
        CourseDto course = new CourseDto(courseName, "Description");
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(13, 0);
        when(viewMock.getSentenceFromUser(
                NEW_LINE + "Enter the name of the course you want to delete:" + NON_BREAKING_SPACE))
                .thenReturn(courseName);
        when(serviceFacadeMock.getCourseByName(courseName)).thenReturn(course);
        when(viewMock.getConfirmationAboutDeletingCourse(course)).thenReturn("N");

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete the course by its name. All available courses:");
        verify(viewMock).printMessage("The course was not deleted." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatEnteredNotExistentOption_whenSelectedThirteenthOptionAndCourseWithGivenNameExistAndUserEnteredNoExistentOption() {
        String courseName = "CourseName";
        CourseDto course = new CourseDto(courseName, "Description");
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(13, 0);
        when(viewMock.getSentenceFromUser(
                NEW_LINE + "Enter the name of the course you want to delete:" + NON_BREAKING_SPACE))
                .thenReturn(courseName);
        when(serviceFacadeMock.getCourseByName(courseName)).thenReturn(course);
        when(viewMock.getConfirmationAboutDeletingCourse(course)).thenReturn("NotExistentOption");

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete the course by its name. All available courses:");
        verify(viewMock).printMessage("There is no such option." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoCourseWithGivenName_whenSelectedThirteenthOptionAndCourseWithGivenNameNotExist() {
        String courseName = "CourseName";
        CourseDto course = null;
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(13, 0);
        when(viewMock.getSentenceFromUser(
                NEW_LINE + "Enter the name of the course you want to delete:" + NON_BREAKING_SPACE))
                .thenReturn(courseName);
        when(serviceFacadeMock.getCourseByName(courseName)).thenReturn(course);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete the course by its name. All available courses:");
        verify(viewMock).printMessage("There is no course with this name." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoOptionThatCorrespondToEnteredNumber_whenEnteredOptionWhichNotExist() {
        when(viewMock.getIntNumberFromUser(NEW_LINE + SELECTION)).thenReturn(16, 0);

        controller.runSchoolApp();

        verify(viewMock).printMessage("There is no option that matches this number." + NEW_LINE);
    }

}
