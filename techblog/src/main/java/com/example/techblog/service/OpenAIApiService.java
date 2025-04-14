package com.example.techblog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenAIApiService implements AIService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    @Override
    public String summarize(String blogContent) {
        String url = aiServiceUrl + "/summarize";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("title", "Some title");
        requestBody.put("content", blogContent);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return (String) response.getBody().get("summary");
    }

    @Override
    public List<String> generateTags(String blogContent) {
        String url = aiServiceUrl + "/generate-tags";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("content", blogContent);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Object tagsObj = response.getBody().get("tags");
        if (tagsObj instanceof List<?>) {
            return ((List<?>) tagsObj).stream()
                    .map(Object::toString)
                    .toList();
        }

        return Collections.emptyList();
    }
    
	/*
	 * @Override public String generatePost(String prompt) { Map<String, String>
	 * request = Map.of("prompt", prompt); ResponseEntity<String> response =
	 * restTemplate.postForEntity( aiServiceUrl + "/generate", request,
	 * String.class); return response.getBody(); }
	 */
}
