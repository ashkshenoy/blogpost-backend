package com.example.techblog.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.techblog.model.Category;
import com.example.techblog.model.Tag;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

public class CreatePostRequest {
    private String title;
    private String content;
    private String category;  // Changed from List<Category> to String
    private Set<String> tags = new HashSet<>();  // Changed to Set<String>

    // Constructor
    public CreatePostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Default constructor
    public CreatePostRequest() {}

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}