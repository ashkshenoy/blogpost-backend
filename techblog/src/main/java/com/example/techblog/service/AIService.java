package com.example.techblog.service;

import java.util.List;

public interface AIService {
    String summarize(String blogContent);
    List<String> generateTags(String blogContent);
    //String generatePost(String prompt);
}
