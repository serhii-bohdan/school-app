package ua.foxminded.schoolapp;

import java.util.Scanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The AppConfig class is a Spring Boot configuration class that defines beans
 * and other settings for the school application.
 * <p>
 * This class is used to configure the {@link Scanner} bean, which provides
 * input reading from the console.
 * <p>
 * The class is annotated with {@link Configuration} and {@link ComponentScan},
 * marking it as a Spring configuration class and specifying the base package to
 * scan for components for automatic dependency detection.
 * <p>
 * The {@link Bean} annotated method is used to create the {@link Scanner} bean,
 * which will read input from the console (System.in). This bean can be used in
 * other components of the application to get user input.
 * <p>
 * Note: The AppConfig class should be used in conjunction with the Spring
 * context to configure the application and define other beans.
 *
 * @author Serhii Bohdan
 */
@Configuration
@ComponentScan(basePackages = "ua.foxminded.schoolapp")
public class ApplicationConfig {

    /**
     * Method to create the {@link Scanner} bean, which reads user input from the
     * console.
     *
     * @return The {@link Scanner} object that will be used for reading user input
     *         from the console.
     */
    @Bean
    Scanner scanner() {
        return new Scanner(System.in);
    }

}
