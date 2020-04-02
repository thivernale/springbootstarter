package org.thivernale.springbootstarter.hello;

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
}
