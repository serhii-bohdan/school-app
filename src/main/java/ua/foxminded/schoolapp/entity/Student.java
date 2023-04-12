package ua.foxminded.schoolapp.entity;

public class Student {

    private int studentId;
    private int groupId;
    private String firstName;
    private String lastName;

    public Student(int groupId, String firstName, String lastName) {
        super();
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getStudentId() {
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
