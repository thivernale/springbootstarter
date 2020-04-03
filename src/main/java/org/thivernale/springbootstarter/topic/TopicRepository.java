package org.thivernale.springbootstarter.topic;

import org.springframework.data.repository.CrudRepository;

/**
 * For CRUD operations we don't have to implement the data layer ourselves
 * but we can use CrudRepository.
 * Specify generic types for entity class and for entity id field.
 */
public interface TopicRepository extends CrudRepository<Topic, String> {

}
