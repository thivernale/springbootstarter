package org.thivernale.springbootstarter.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thivernale.springbootstarter.topic.Topic;

/**
 * The web layer of a Spring application leverages Spring MVC.
 * It lets you build server-side code which maps to URLs and provides responses.
 *
 */
@RestController
public class CourseController {
    /**
     * Declare a dependency on a service and tell Spring to inject it
     */
    @Autowired
    private CourseService courseService;

    /**
     * Maps to a GET request by default.
     * The result is going to be converted automatically to JSON
     * because of the @RestController annotation.
     * @return
     */
    @RequestMapping("/topics/{id}/courses")
    public List<Course> getCourses(@PathVariable String id) {
        return courseService.getCourses(id);
    }

    /**
     * Specify a variable path using curly braces, specify that value should be
     * passed as parameter using @PathVariable annotation for parameter.
     * Pass parameter if names do not match.
     * @param id
     * @return
     */
    @RequestMapping("/topics/{topicId}/courses/{id}")
    public Course getCourse(@PathVariable String id) {
        return courseService.getCourse(id);
    }

    /**
     * Add a new topic using POST method.
     * Specify method in annotation, use RequestMethod enum.
     * Pick the Course instance from the Request body and convert it to an
     * object of that class.
     * @param topicId
     */
    @RequestMapping(method = {RequestMethod.POST}, path = "/topics/{topicId}/courses")
    public void addCourse(@RequestBody Course course, @PathVariable String topicId) {
        course.setTopic(new Topic(topicId, "", ""));
        courseService.addCourse(course);
    }

    @RequestMapping(method = {RequestMethod.PUT}, path = "/topics/{topicId}/courses/{id}")
    public void updateCourse(@RequestBody Course course, @PathVariable String topicId, @PathVariable String id) {
        course.setTopic(new Topic(topicId, "", ""));
        courseService.updateCourse(course);
    }

    @RequestMapping(method = {RequestMethod.DELETE}, path = "/topics/{topicId}/courses/{id}")
    public void deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
    }
}
