package org.thivernale.springbootstarter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.thivernale.springbootstarter.course.CourseRepository;
import org.thivernale.springbootstarter.security.EmployeeRepository;
import org.thivernale.springbootstarter.security.UserRepository;
import org.thivernale.springbootstarter.security.models.Address;
import org.thivernale.springbootstarter.security.models.Employee;
import org.thivernale.springbootstarter.security.models.User;
import org.thivernale.springbootstarter.security.models.UserRole;
import org.thivernale.springbootstarter.topic.TopicRepository;

import com.github.javafaker.Faker;

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
     * @param employeeRepository
     * @return
     */
    @Bean
    public CommandLineRunner demo(UserRepository userRepository, EmployeeRepository employeeRepository) {
        return (args) -> {
            log.info("CommandLineRunner Running...");

            //demoInner(session);

            Faker faker = new Faker();

            for (User u : userRepository.findAll()) {
                log.info(String.format("User %d %s %s found!", u.getId(), u.getUserName(), u.getRoles()));

                Set<Address> listOfAddresses = new HashSet<>();
                Address addr = new Address(faker);
                Address addr2 = addr.clone();

                // set some values
                addr.setCity(addr.getCity());
                addr.setStreet(addr.getStreet());
                addr.setState(addr.getState());
                addr.setZipCode(addr.getZipCode());
                addr2.setCity(addr2.getCity());
                addr2.setStreet(addr2.getStreet());
                addr2.setState(addr2.getState());
                addr2.setZipCode(addr2.getZipCode());

                listOfAddresses.addAll(Arrays.asList(new Address[]{addr, addr2}));
                u.getListOfAddresses();
                // set into list
                u.setListOfAddresses(listOfAddresses);
                // set into embedded value objects
                u.setAddress((Address) listOfAddresses.toArray()[0]);
                u.setOfficeAddress((Address) listOfAddresses.toArray()[1]);

                Collection<UserRole> userRoles = u.getUserRoles();
                if (userRoles == null) {
                    u.setUserRoles(new ArrayList<UserRole>());
                }
                if (userRoles.size() == 0 || u.getRoles() == null) {
                    String[] roleNames = {"ROLE_USER", "ROLE_DEFAULT", "ROLE_GUEST"};
                    u.setRoles(String.join(",", roleNames));
                    Arrays.asList(roleNames).forEach(roleName -> userRoles.add(new UserRole(roleName)));
                }
                System.out.println(u.getUserRoles().size());

                System.out.println("Employee:");
                Optional<Employee> oldEmployee = Optional.ofNullable(u.getEmployee());
                System.out.println(oldEmployee.toString());

                Employee employee = new Employee();
                employee.setPosition("Customer Support " + u.getId());
                employee.setDisplayName(u.getDisplayName());

                u.setEmployee(employee);
                // establish the link in the other direction
                employee.setUser(u);

                userRepository.save(u);
                /*employeeRepository.save(employee);*/
                if (!oldEmployee.isEmpty()) {
                    oldEmployee.get().setUser(null);
                    employeeRepository.delete(oldEmployee.get());
                }

                log.info(
                    "Addresses:\n" +
                        String.join("\n", u.getListOfAddresses().stream().map(a -> a.city()).collect(Collectors.toList())) + "\n" +
                        u.getAddress().city() + "\n" +
                        u.getOfficeAddress().city() + "\n" +
                        "Roles:\n" +
                        String.join("\n", u.getUserRoles().stream().map(a -> a.getAuthority()).collect(Collectors.toList())) +
                        "\n"
                    );
            }
        };
    }

    private void demoInner(Session session) {
        //SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        //Session session = sessionFactory.openSession();

        session.beginTransaction();
        System.out.println("IN");
        session.getTransaction().commit();
        session.close();
    }
}
