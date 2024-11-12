package org.thivernale.springbootstarter;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.thivernale.springbootstarter.course.Course;
import org.thivernale.springbootstarter.course.CourseRepository;
import org.thivernale.springbootstarter.topic.Topic;
import org.thivernale.springbootstarter.topic.TopicRepository;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Profile("!dev")
public class CourseApiInitializer implements CommandLineRunner {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        List<String> coveredTopics = Arrays.asList(
            "accent", "algol_68", "c", "c_", "c__", "coffeescript", "go", "groovy",
            "java", "javascript", "kotlin", "php", "postscript",
            "python", "r", "ruby", "scala", "swift", "typescript", "xl", "xpl"
        );

        for (int i = 0; i < 10; i++) {
            String name = faker.programmingLanguage()
                .name();
            String id = name.toLowerCase()
                .replaceAll("\\s", "-")
                .replaceAll("[^\\w]", "_");
            String description = name + " Description by " + faker.programmingLanguage()
                .creator();

            if (coveredTopics.contains(id)) {
                topicRepository.save(new Topic(id, name, description));
                courseRepository.save(new Course(id + "-course", name, description, id));
            }
        }
    }

    @SuppressWarnings("unused")
    private void stringManipulationLearning() {
        String[] arr = {"Larry", "Moe", "Curly"};
        List<String> stooges = Arrays.asList(arr);

        final Function<? super String, ? extends String> rr = el -> el.toUpperCase() + " TH";
        final Function<? super String, ? extends String> rr2 = String::toUpperCase;

        stooges = stooges.stream()
            .map(rr)
            .collect(Collectors.toList());

        //in-place replacement:
        //stooges.replaceAll(String::toUpperCase);

        System.out.println(String.join(", ", stooges));
        stooges.forEach(System.out::println);
    }
}
