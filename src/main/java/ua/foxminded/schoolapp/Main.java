package ua.foxminded.schoolapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ua.foxminded.schoolapp.cli.SchoolController;

@SpringBootApplication
public class Main {

    private final SchoolController controller;

    public Main(SchoolController controller) {
        this.controller = controller;
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        Main main = context.getBean(Main.class);
        main.run();
    }

    public void run() {
        controller.runSchoolApp();
    }

}
