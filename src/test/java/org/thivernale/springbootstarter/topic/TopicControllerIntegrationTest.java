package org.thivernale.springbootstarter.topic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.JsonNode;

@ExtendWith(value = { SpringExtension.class })
// start the whole Spring Boot context and the embedded database
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopicControllerIntegrationTest {
    @LocalServerPort
    int randomServerPort;

    private TestRestTemplate testRestTemplate;

    @BeforeEach
    private void setUp() {
        testRestTemplate = new TestRestTemplate();
    }

    @Test
    public void deleteKnownTopicShouldReturn404AfterDeletion() {

        String baseUrl = String.format("http://localhost:%d", randomServerPort);

        JsonNode allTopics = this.testRestTemplate.getForObject(baseUrl + "/topics", JsonNode.class);
        String topicId = allTopics.get(0).get("id").asText();

        String topicUrl = baseUrl + "/topics/" + topicId;

        System.out.println("Deleting topic with id: " + topicId + " !!!!!!!!!!!" + topicUrl);

        ResponseEntity<JsonNode> responseOne = this.testRestTemplate
            .getForEntity(topicUrl, JsonNode.class);

        assertThat(responseOne.getStatusCode(), is(HttpStatus.OK));

        testRestTemplate.delete(topicUrl);

        ResponseEntity<JsonNode> responseTwo = this.testRestTemplate
            .getForEntity(topicUrl, JsonNode.class);

        assertThat(responseTwo.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
