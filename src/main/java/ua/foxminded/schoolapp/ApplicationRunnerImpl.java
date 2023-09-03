package ua.foxminded.schoolapp;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.foxminded.schoolapp.cli.SchoolView;
import ua.foxminded.schoolapp.service.logic.ServiceFacade;

/**
 * The ApplicationRunnerImpl class implements the Spring Boot
 * {@link ApplicationRunner} interface. It provides a mechanism to run custom
 * code when the Spring Boot application starts.
 * <p>
 * This class is annotated with {@code @Component}, indicating that it is a
 * Spring component and should be automatically discovered and registered as a
 * bean in the Spring context.
 *
 * @author Serhii Bohdan
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    /**
     * The logger for logging events and messages in the
     * {@link ApplicationRunnerImpl} class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRunnerImpl.class);

    private final ServiceFacade serviceFacade;
    private final SchoolView view;

    /**
     * Constructs an instance of {@code ApplicationRunnerImpl}.
     *
     * @param serviceFacade The {@link ServiceFacade} to be used for initializing
     *                      the application schema.
     * @param view          The {@link SchoolView} responsible for displaying the
     *                      application menu.
     */
    public ApplicationRunnerImpl(ServiceFacade serviceFacade, SchoolView view) {
        this.serviceFacade = serviceFacade;
        this.view = view;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Initializes the application schema during application startup by invoking the
     * {@link ServiceFacade#initSchema()} method. Also displays the application menu
     * using the {@link SchoolView#showMenu()} method.
     *
     * @param args the application arguments passed to the application
     * @throws Exception if an exception occurs during schema initialization
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("Initialization before starting the application");
        serviceFacade.initSchema();

        LOGGER.info("Displaying the application menu");
        view.showMenu();
    }

}
