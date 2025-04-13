package com.example.techblog.dto;

public class AIPostRequest {
    private String title;
    private String prompt;

    public AIPostRequest() {
    }

    public AIPostRequest(String title, String prompt) {
        this.title = title;
        this.prompt = prompt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}