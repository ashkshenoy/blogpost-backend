package com.example.techblog.dto;

import java.time.LocalDateTime;
import com.example.techblog.model.Like;

public class LikeDTO {
    private Long id;
    private UserDto user;
    private LocalDateTime createdAt;

    public static LikeDTO fromEntity(Like like) {
        LikeDTO dto = new LikeDTO();
        dto.setId(like.getId());
        dto.setUser(UserDto.fromEntity(like.getUser()));
        dto.setCreatedAt(like.getCreatedAt());
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}