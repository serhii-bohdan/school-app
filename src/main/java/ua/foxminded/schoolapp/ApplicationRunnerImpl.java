package ua.foxminded.schoolapp;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolapp.service.logic.ServiceFacade;

/**
 * The ApplicationRunnerImpl class implements the Spring Boot
 * {@link ApplicationRunner} interface. It provides a mechanism to run custom
 * code when the Spring Boot application starts.
 * <p>
 * This class is annotated with {@code @Component}, indicating that it is a
 * Spring component and should be automatically discovered and registered as a
 * bean in the Spring context. It requires an instance of {@link ServiceFacade}
 * to perform the initialization of the application schema upon application
 * startup.
 *
 * @author Serhii Bohdan
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final ServiceFacade serviceFacade;

    /**
     * Constructs a new ApplicationRunnerImpl with the specified ServiceFacade.
     *
     * @param serviceFacade the ServiceFacade to be used for initializing the
     *                      application schema
     */
    public ApplicationRunnerImpl(ServiceFacade serviceFacade) {
        this.serviceFacade = serviceFacade;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Initializes the application schema during application startup by invoking the
     * {@link ServiceFacade#initSchema()} method.
     *
     * @param args the application arguments passed to the application
     * @throws Exception if an exception occurs during schema initialization
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        serviceFacade.initSchema();
    }

}
