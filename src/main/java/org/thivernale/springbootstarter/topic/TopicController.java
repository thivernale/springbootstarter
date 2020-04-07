package org.thivernale.springbootstarter.topic;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<List<Topic>> getTopics() {
        return ResponseEntity.ok(topicService.getTopics());
    }

    /**
     * Specify a variable path using curly braces, specify that value should be
     * passed as parameter using @PathVariable annotation for parameter.
     * Pass parameter if names do not match.
     * @param id
     * @return
     */
    @RequestMapping("/topics/{id}")
    public ResponseEntity<Topic> getTopic(@PathVariable("id") String id) {
        return ResponseEntity.ok(topicService.getTopic(id));
    }

    /**
     * Add a new topic using POST method.
     * Specify method in annotation, use RequestMethod enum.
     * Pick the topic instance from the Request body and convert it to an
     * object of that class.
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, path = "/topics")
    public ResponseEntity<Void> addTopic(@Valid @RequestBody Topic topic, UriComponentsBuilder uriComponentsBuilder) {
        topicService.addTopic(topic);

        UriComponents uriComponents = uriComponentsBuilder.path("/topics/{id}").buildAndExpand(topic.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = {RequestMethod.PUT}, path = "/topics/{id}")
    public ResponseEntity<Topic> updateTopic(@Valid @RequestBody Topic topic, @PathVariable("id") String id) {
        return ResponseEntity.ok(topicService.updateTopic(id, topic));
    }

    @RequestMapping(method = {RequestMethod.DELETE}, path = "/topics/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable String id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok().build();
    }
}
