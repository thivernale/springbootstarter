package org.thivernale.springbootstarter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.thivernale.springbootstarter.course.CourseRepository;
import org.thivernale.springbootstarter.security.UserRepository;
import org.thivernale.springbootstarter.security.models.User;
import org.thivernale.springbootstarter.topic.TopicRepository;

/**
 * This is where the application will be bootstrapped.
 * Spring Boot creates a standalone application, that means it does not need a
 * Servlet container, it does not need to be deployed on a server.
 * This class can be started like any other program that has a main method,
 * it creates a servlet container, starts it and hosts the application in that
 * servlet container.
 */

@EnableOAuth2Sso()
/**
 * Annotation tells Spring Boot that this is the starting point for the
 * application
 */
@SpringBootApplication
/**
 * Enable JPA repositories
 */
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, TopicRepository.class, CourseRepository.class})
public class CourseApiApp {

    private static final Logger log = LoggerFactory.getLogger(CourseApiApp.class);

    public static void main(String[] args) {
        // Spring Boot utility - execute starting method
        // - sets up default configuration
        // - starts Spring application context (Spring acts as a container for our services)
        // - performs class path scan (plugs in our classes annotated with certain)
        // - starts Tomcat server
        SpringApplication.run(CourseApiApp.class, args);
    }

    /**
     * @see https://spring.io/guides/gs/accessing-data-jpa/
     * @param userRepository
     * @return
     */
    @Bean
    public CommandLineRunner demo(UserRepository userRepository) {
        return (args) -> {
            log.info("Running...");
            for (User u : userRepository.findAll()) {
                log.info(String.format("User %d %s %s found!", u.getId(), u.getUserName(), u.getRoles()));
            }
        };
    }
}
