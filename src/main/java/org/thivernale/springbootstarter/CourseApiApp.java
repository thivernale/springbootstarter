package org.thivernale.springbootstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is where the application will be bootstrapped.
 * Spring Boot creates a standalone application, that means it does not need a
 * Servlet container, it does not need to be deployed on a server.
 * This class can be started like any other program that has a main method,
 * it creates a servlet container, starts it and hosts the application in that
 * servlet container.
 */

/**
 * Annotation tells Spring Boot that this is the starting point for the
 * application
 *
 */
@SpringBootApplication
public class CourseApiApp {

    public static void main(String[] args) {
        // Spring Boot utility - execute starting method
        // - sets up default configuration
        // - starts Spring application context (Spring acts as a container for our services)
        // - performs class path scan (plugs in our classes annotated with certain)
        // - starts Tomcat server
        SpringApplication.run(CourseApiApp.class, args);
    }

}
