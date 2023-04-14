package ua.foxminded.schoolapp.entity;

import ua.foxminded.schoolapp.dao.StudentDAO;
import ua.foxminded.schoolapp.dao.implement.StudentDAOImpl;

public class Student {

    private int studentId;
    private int groupId;
    private String firstName;
    private String lastName;
    private StudentDAO studentDao = new StudentDAOImpl();

    public Student(int groupId, String firstName, String lastName) {
        super();
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getStudentId() {
        this.studentId = studentDao.findStudentIdByNameAndGroupId(firstName, lastName, groupId);
        return studentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", groupId=" + groupId + ", firstName=" + firstName + ", lastName="
                + lastName + "]";
    }

}
