package org.thivernale.springbootstarter;

import com.github.javafaker.Faker;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thivernale.springbootstarter.security.EmployeeRepository;
import org.thivernale.springbootstarter.security.UserRepository;
import org.thivernale.springbootstarter.security.models.Address;
import org.thivernale.springbootstarter.security.models.Employee;
import org.thivernale.springbootstarter.security.models.User;
import org.thivernale.springbootstarter.security.models.UserRole;

import javax.persistence.EntityManagerFactory;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is where the application will be bootstrapped.
 * Spring Boot creates a stand-alone application, that means it does not need a
 * Servlet container, it does not need to be deployed on a server.
 * This class can be started like any other program that has a main method,
 * it creates a servlet container, starts it and hosts the application in that
 * servlet container.
 */

@SpringBootApplication
/**
 * Enable JPA repositories
 */
@EnableJpaRepositories(
    //basePackageClasses = {UserRepository.class, TopicRepository.class, CourseRepository.class},
    basePackages = "org.thivernale.springbootstarter"
)
@EnableTransactionManagement()
public class CourseApiApp {
    private static final Logger log = LoggerFactory.getLogger(CourseApiApp.class);
    @Autowired
    EntityManagerFactory entityManagerFactory;

    public static void main(String[] args) {
        // Spring Boot utility - execute starting method
        // - sets up default configuration
        // - starts Spring application context (Spring acts as a container for our services)
        // - performs class path scan (plugs in our classes annotated with certain)
        // - starts Tomcat server
        SpringApplication.run(CourseApiApp.class, args);
    }

    /**
     * @see <a href="https://spring.io/guides/gs/accessing-data-jpa/">Accessing Data JPA Guide</a>
     */
    @Bean
    public CommandLineRunner demo(UserRepository userRepository, EmployeeRepository employeeRepository) {
        return (args) -> {
            log.info("CommandLineRunner Running...");

            if (hibernateDemoInner()) return;

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

                listOfAddresses.addAll(Arrays.asList(addr, addr2));
                u.getListOfAddresses();
                // set into list
                u.setListOfAddresses(listOfAddresses);
                // set into embedded value objects
                u.setAddress((Address) listOfAddresses.toArray()[0]);
                u.setOfficeAddress((Address) listOfAddresses.toArray()[1]);

                Collection<UserRole> userRoles = u.getUserRoles();
                if (userRoles == null) {
                    u.setUserRoles(new ArrayList<>());
                }
                if (userRoles.isEmpty() || u.getRoles() == null) {
                    String[] roleNames = {"ROLE_USER", "ROLE_DEFAULT", "ROLE_GUEST"};
                    u.setRoles(String.join(",", roleNames));
                    Arrays.asList(roleNames)
                        .forEach(roleName -> userRoles.add(new UserRole(roleName)));
                }
                System.out.println(u.getUserRoles()
                    .size());

                System.out.println("Employee:");
                Optional<Employee> oldEmployee = Optional.ofNullable(u.getEmployee());
                System.out.println(oldEmployee);

                Employee employee = new Employee();
                employee.setPosition("Customer Support " + u.getId());
                employee.setDisplayName(u.getDisplayName());

                u.setEmployee(employee);
                // establish the link in the other direction
                employee.setUser(u);

                userRepository.save(u);
                /*employeeRepository.save(employee);*/
                if (oldEmployee.isPresent()) {
                    oldEmployee.get()
                        .setUser(null);
                    employeeRepository.delete(oldEmployee.get());
                }

                log.info(
                    "Addresses:\n" +
                        u.getListOfAddresses()
                            .stream()
                            .map(com.github.javafaker.Address::city)
                            .collect(Collectors.joining("\n")) + "\n" +
                        u.getAddress()
                            .city() + "\n" +
                        u.getOfficeAddress()
                            .city() + "\n" +
                        "Roles:\n" +
                        u.getUserRoles()
                            .stream()
                            .map(UserRole::getAuthority)
                            .collect(Collectors.joining("\n")) +
                        "\n"
                );
            }
        };
    }

    private boolean hibernateDemoInner() {
        //SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        // the class name, not the table name
        Query<User> query = session.createQuery("from User where id >= 1", User.class);

        // get a named query specified by annotation in Entity class
        query = session.getNamedQuery("User.byId");
        query.setParameter(0, 2);

        // apply pagination
        query.setFirstResult(0)
            .setMaxResults(1);
        List<User> users = query.list();

        Query<String> queryName = session.createQuery("select userName from User", String.class);
        queryName.setCacheable(true);
        List<String> userNames = queryName.list();
        System.out.println(String.join(", ", userNames.toString()));

        // get a list of Map objects
        String q3 = "select new map(id, userName) from User where id > ?0 and added IS NULL";
        q3 = q3.replace("?0", ":idParam");

        Query<Map<Integer, String>> queryMap = session.createQuery(q3);
        //queryMap.setParameter(0, Integer.parseInt("1"));
        queryMap.setParameter("idParam", Integer.parseInt("-1"));
        //queryMap.setParameter(1, null);
        List<Map<Integer, String>> resultMap = queryMap.list();
        for (Map<Integer, String> mapEntry : resultMap) {
            System.out.println(mapEntry);
        }

        // Criteria API
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("userName", "admin"));
        //entityManagerFactory.getCriteriaBuilder().equal(null, null);

        User exampleUser = new User();
        exampleUser.setActive(true);
        exampleUser.setUserName("random user name");
        Example example = Example.create(exampleUser)
            .excludeProperty("userName");
        criteria.add(example);

        //users = criteria.list();

        User adminUser = session.get(User.class, "admin");
        System.out.println(adminUser != null ? adminUser.getId() : "admin user not found");

        session.getTransaction()
            .commit();
        session.close();

        System.out.println("Printing after session is closed:");
        for (User user : users) {
            System.out.println(user.getUserName());
        }

        System.out.println("------------------");

        // open session second time
        session = sessionFactory.openSession();
        session.beginTransaction();

        // run previous queries again
        adminUser = session.get(User.class, "admin");
        System.out.println(adminUser != null ? adminUser.getId() : "admin user not found");

        queryName = session.createQuery("select userName from User", String.class);
        queryName.setCacheable(true);
        userNames = queryName.list();

        session.getTransaction()
            .commit();
        session.close();

        return true;
    }
}
