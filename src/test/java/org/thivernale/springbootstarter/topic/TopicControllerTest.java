package org.thivernale.springbootstarter.topic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(value = { SpringExtension.class })
@WebMvcTest(TopicController.class)
public class TopicControllerTest {
    // autowire an instance of MockMvc to use for calling an endpoint
    @Autowired
    private MockMvc mockMvc;

    // needed to serialize object into JSON
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TopicService topicService;

    @Captor
    private ArgumentCaptor<Topic> argumentCaptor;

    @Test
    public void postingANewTopicShouldCreateItInDatabase() throws Exception {
        Topic topic = new Topic("java11", "Java 11", "Java 11 Desc");

        // pass data object to the service class which will create the entity in
        // the back-end
        // when the method is called, capture the passed argument
        when(topicService.addTopic(argumentCaptor.capture())).thenReturn("java11");

        // send JSON data to the backend
        // perform MockMvc test
        this.mockMvc.perform(
            post("/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(topic))
            )
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "http://localhost/topics/java11"));

        assertThat(argumentCaptor.getValue().getId()).isEqualTo("java11");
        assertThat(argumentCaptor.getValue().getName()).isEqualTo("Java 11");
    }

    @Test
    public void allTopicsEndpointShouldReturnTwoTopics() throws Exception {
        // mock service
        /**
         * @note: List.of() requires Java 9+, otherwise use Arrays.asList() for
         *        the same purpose
         */
        when(topicService.getTopics())
        .thenReturn(
            List.of(
                createEntity("php", "PHP", "PHP Course"),
                createEntity("es6", "EcmaScript 6", "EcmaScript 6 Course")
                )
            );

        // perform GET request
        this.mockMvc.perform(get("/topics"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is("php")))
        .andExpect(jsonPath("$[0].name", is("PHP")))
        .andExpect(jsonPath("$[0].description", is("PHP Course")));
    }

    @Test
    public void getJavaTopicShouldReturnATopic() throws Exception {
        when(topicService.getTopic("java"))
        .thenReturn(createEntity("java", "Java", "Java Course"));

        // perform GET request
        this.mockMvc.perform(get("/topics/java"))
        // assert the response
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is("java")))
        .andExpect(jsonPath("$.name", is("Java")))
        .andExpect(jsonPath("$.description", is("Java Course")));
    }

    @Test
    public void getUnknownTopicShouldReturn404() throws Exception {
        when(topicService.getTopic("any-name"))
        .thenThrow(
            new TopicNotFoundException("Topic 'any-time' Not Found")
            );

        // perform GET request
        this.mockMvc.perform(get("/topics/any-name"))
        .andExpect(status().isNotFound());
    }

    @Test
    public void updateKnowTopicShouldUpdateATopic() throws Exception {
        Topic topic = new Topic("java11", "Java 11 Updated", "Java 11 Desc Updated");

        // mock service
        when(topicService.updateTopic(eq("java11"), argumentCaptor.capture()))
        .thenReturn(createEntity("java11", "Java 11 Updated", "Java 11 Desc Updated"));

        this.mockMvc.perform(
            // prepare the request
            put("/topics/java11")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsString(topic))
            )
        // assert the response
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is("java11")))
        .andExpect(jsonPath("$.name", is("Java 11 Updated")))
        .andExpect(jsonPath("$.description", is("Java 11 Desc Updated")))
        ;

        // assert that the captured object matches the one passed
        assertThat(argumentCaptor.getValue().getId()).isEqualTo("java11");
        assertThat(argumentCaptor.getValue().getName()).isEqualTo("Java 11 Updated");
    }

    @Test
    public void updateUnknownTopicShouldReturn404() throws Exception {
        Topic topic = new Topic("java11", "Java 11 Updated", "Java 11 Desc Updated");

        // mock service
        when(topicService.updateTopic(eq("java15"), argumentCaptor.capture()))
        .thenThrow(new TopicNotFoundException(String.format("Topic with id: '%s' not found.", "java15")));

        this.mockMvc.perform(
            // prepare the request
            put("/topics/java15")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsString(topic))
            )
        // assert the response
        .andExpect(status().isNotFound());
    }

    private Topic createEntity(String id, String name, String description) {
        return new Topic(id, name, description);
    }
}
