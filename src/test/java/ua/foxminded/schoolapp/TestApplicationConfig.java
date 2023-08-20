package ua.foxminded.schoolapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration(proxyBeanMethods = false)
@ComponentScan(basePackages = "ua.foxminded.schoolapp")
public class TestApplicationConfig {

    public static void main(String[] args) {
        SpringApplication.from(Main::main).with(TestApplicationConfig.class).run(args);
    }

}
