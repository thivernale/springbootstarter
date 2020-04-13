package org.thivernale.springbootstarter.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web layer of a Spring application leverages Spring MVC.
 * It lets you build server-side code which maps to URLs and provides responses.
 *
 */
@RestController
public class HelloController {
    /**
     * Maps to a GET request by default
     * @return
     */
    @RequestMapping("/hello")
    public String sayHi() {
        return "Hi";
    }

    /**
     * Authorization Use Case 1:
     *
     * @return
     */
    @GetMapping("/")
    public String home() {
        return ("<h1>Welcome</h1>");
    }

    /**
     * Authorization Use Case 2:
     *
     * @return
     */
    @GetMapping("/user")
    public String user() {
        return ("<h1>Welcome User</h1>");
    }

    /**
     * Authorization Use Case 3:
     *
     * @return
     */
    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome Admin</h1>");
    }
}
