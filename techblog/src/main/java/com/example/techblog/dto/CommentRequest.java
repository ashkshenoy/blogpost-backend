package com.example.techblog.dto;


public class CommentRequest {
    private String content;

    // Default constructor
    public CommentRequest() {}

    // Constructor with content
    public CommentRequest(String content) {
        this.content = content;
    }

    // Getter and Setter
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}