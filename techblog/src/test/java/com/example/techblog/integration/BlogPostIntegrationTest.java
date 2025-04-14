package com.example.techblog.integration;

import com.example.techblog.dto.CreatePostRequest;
import com.example.techblog.repository.BlogPostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogPostIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Test
    void shouldCreateAndFetchPost() {
        String baseUrl = "http://localhost:" + port + "/api/posts";

        CreatePostRequest request = new CreatePostRequest();
        request.setTitle("Integration Test Title");
        request.setContent("This is the body of the post");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("ash", "password"); // If using basic auth

        HttpEntity<CreatePostRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> fetchResponse = restTemplate.getForEntity(baseUrl, String.class);
        assertThat(fetchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetchResponse.getBody()).contains("Integration Test Title");
    }
}
