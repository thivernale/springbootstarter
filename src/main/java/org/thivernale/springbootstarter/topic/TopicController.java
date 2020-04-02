package org.thivernale.springbootstarter.topic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web layer of a Spring application leverages Spring MVC.
 * It lets you build server-side code which maps to URLs and provides responses.
 *
 */
@RestController
public class TopicController {
    /**
     * Declare a dependency on a service and tell Spring to inject it
     */
    @Autowired
    private TopicService topicService;

    /**
     * Maps to a GET request by default.
     * The result is going to be converted automatically to JSON
     * because of the @RestController annotation.
     * @return
     */
    @RequestMapping("/topics")
    public List<Topic> getTopics() {
        return topicService.getTopics();
    }

    /**
     * Specify a variable path using curly braces, specify that value should be
     * passed as parameter using @PathVariable annotation for parameter.
     * Pass parameter if names do not match.
     * @param id
     * @return
     */
    @RequestMapping("/topics/{id}")
    public Topic getTopic(@PathVariable String id) {
        return topicService.getTopic(id);
    }

    /**
     * Add a new topic using POST method.
     * Specify method in annotation, use RequestMethod enum.
     * Pick the topic instance from the Request body and convert it to an
     * object of that class.
     */
    @RequestMapping(method = {RequestMethod.POST}, path = "/topics")
    public void addTopic(@RequestBody Topic topic) {
        topicService.addTopic(topic);
    }

    @RequestMapping(method = {RequestMethod.PUT}, path = "/topics/{id}")
    public void updateTopic(@RequestBody Topic topic, @PathVariable String id) {
        topicService.updateTopic(id, topic);
    }

    @RequestMapping(method = {RequestMethod.DELETE}, path = "/topics/{id}")
    public void deleteTopic(@PathVariable String id) {
        topicService.deleteTopic(id);
    }
}
