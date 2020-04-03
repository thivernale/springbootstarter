package org.thivernale.springbootstarter.course;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * For CRUD operations we don't have to implement the data layer ourselves
 * but we can use CrudRepository.
 * Specify generic types for entity class and for entity id field.
 */
public interface CourseRepository extends CrudRepository<Course, String> {

    /**
     * Define method declarations. Spring Data JPA Framework figures out based
     * on method name what its implementation should be.
     */
    // filter by a String property
    List<Course> findByName(String anyName);
    // filter by property ("id") of an object property ("topic")
    List<Course> findByTopicId(String topicId);
}
