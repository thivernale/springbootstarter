package org.thivernale.springbootstarter.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * We want a business service to be a singleton: when the application starts, an
 * instance of it is created and it is later used by other class instances which
 * depend on it.
 *
 */
@Service
public class CourseService {
    /**
     * When Spring creates an instance of the CourseService it is going to inject
     * an instance of the CourseRepository.
     */
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getCourses(String id) {
        return courseRepository.findByTopicId(id);
    }

    public Course getCourse(String id) {
        return courseRepository.findById(id).get();
    }

    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    public void updateCourse(Course course) {
        courseRepository.save(course);
    }

    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }
}
