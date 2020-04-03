package org.thivernale.springbootstarter.topic;

import java.util.ArrayList;
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
public class TopicService {
    /**
     * When Spring creates an instance of the TopicService it is going to inject
     * an
     * instance of the TopicRepository.
     */
    @Autowired
    private TopicRepository topicRepository;

    /**
     * Arrays.asList creates an immutable variable (fixed-size list) so we need
     * to
     * create a list from it in order to add new elements to it.
     */
    //    private List<Topic> topics = new ArrayList<>(
    //        Arrays.asList(
    //            new Topic("spring", "Spring Framework", "Spring Framework Description"),
    //            new Topic("java", "Core Java", "Core Java Description"),
    //            new Topic("javascript", "JavaScript", "JavaScript Description")
    //            )
    //        );

    public List<Topic> getTopics() {
        List<Topic> topics = new ArrayList<>();
        // connect to the database, run a query, convert rows to Topic
        // instances and get them back
        topicRepository.findAll()
        // use a method reference for a lambda function
        .forEach(topics::add);

        return topics;
    }

    public Topic getTopic(String id) {
        return
            // topics.stream().filter(t -> t.getId().equals(id)).findFirst().get();
            topicRepository.findById(id).get();
    }

    public void addTopic(Topic topic) {
        // topics.add(topic);
        topicRepository.save(topic);
    }

    public void updateTopic(String id, Topic topic) {
        //        for (int i = 0; i < topics.size(); i++) {
        //            Topic t = topics.get(i);
        //            if (t.getId().equals(id)) {
        //                topics.set(i, topic);
        //                return;
        //            }
        //        }
        topicRepository.save(topic);
    }

    public void deleteTopic(String id) {
        // topics.removeIf(t -> t.getId().equals(id));
        topicRepository.deleteById(id);
    }
}
