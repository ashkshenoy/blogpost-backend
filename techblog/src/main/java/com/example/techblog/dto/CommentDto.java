package com.example.techblog.dto;

import java.time.LocalDateTime;

public class CommentDto {
    private Long id;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;

    public CommentDto(Long id, String content, String authorUsername, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
    }
}