package com.example.techblog.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BlogPostDto {
    private Long id;
    private String title;
    private String content;
    private UserDto author;
    private String category;  // Changed from List<String> to single String
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public BlogPostDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public UserDto getAuthor() { return author; }
    public void setAuthor(UserDto author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}