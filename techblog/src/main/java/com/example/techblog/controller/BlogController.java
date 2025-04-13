package com.example.techblog.controller;

import com.example.techblog.service.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    private final AIService aiService;

    public BlogController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/summarize")
    public ResponseEntity<String> summarize(@RequestBody String content) {
        return ResponseEntity.ok(aiService.summarize(content));
    }

    @PostMapping("/generate-tags")
    public ResponseEntity<List<String>> generateTags(@RequestBody String content) {
        return ResponseEntity.ok(aiService.generateTags(content));
    }
}
