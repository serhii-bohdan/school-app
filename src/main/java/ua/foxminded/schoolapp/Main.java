package ua.foxminded.schoolapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ua.foxminded.schoolapp.cli.SchoolController;
import ua.foxminded.schoolapp.service.CourseService;
import ua.foxminded.schoolapp.service.GroupService;
import ua.foxminded.schoolapp.service.StudentService;

@SpringBootApplication
public class Main {

    private GroupService groupService;
    private StudentService studentService;
    private CourseService courseService;
    private SchoolController controller;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        Main main = context.getBean(Main.class);
        main.runSchoolApp();
    }

    public void runSchoolApp() {
        groupService.initGroups();
        studentService.initStudents();
        courseService.initCourses();
        studentService.initStudentsCoursesTable();
        controller.runSchoolApp();
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setController(SchoolController controller) {
        this.controller = controller;
    }

}
