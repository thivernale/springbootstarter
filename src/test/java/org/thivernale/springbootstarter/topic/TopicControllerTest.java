package org.thivernale.springbootstarter.topic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@WebMvcTest(TopicController.class)
@ExtendWith(value = { SpringExtension.class })
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
        // send JSON data to the backend
        Topic topicRequest = new Topic("java11", "Java 11", "Java 11 Desc");

        // pass data object to the service class which will create the entity in the back-end
        // when the method is called, capture the passed argument
        when(topicService.addTopic(argumentCaptor.capture())).thenReturn("java11");

        // perform MockMvc test
        this.mockMvc.perform(post("/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(topicRequest))
            )
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "http://localhost/topics/java11"));

        assertThat(argumentCaptor.getValue().getName()).isEqualTo("Java 11");
    }
}
