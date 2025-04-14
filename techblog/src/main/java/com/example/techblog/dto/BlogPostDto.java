package com.example.techblog.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.techblog.model.BlogPost;
import com.example.techblog.model.Tag;

public class BlogPostDto {
    private Long id;
    private String title;
    private String content;
    private UserDto author;
    private String category;  // Changed from List<String> to single String
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likesCount;
    private boolean hasLiked;
    private Set<LikeDTO> likes;
    // Constructor
    public BlogPostDto() {}
    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }

    public Set<LikeDTO> getLikes() {
        return likes;
    }

    public void setLikes(Set<LikeDTO> likes) {
        this.likes = likes;
    }
    public static BlogPostDto fromEntity(BlogPost post, String currentUsername) {
        if (post == null) return null;

        BlogPostDto dto = new BlogPostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthor(UserDto.fromEntity(post.getAuthor()));
        dto.setCategory(post.getCategory() != null ? post.getCategory().getName() : null);
        
        // Convert Set<Tag> to List<String>
        dto.setTags(post.getTags() != null 
            ? post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList())
            : new ArrayList<>());
        
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        
        // Handle likes
        if (post.getLikes() != null) {
            dto.setLikesCount(post.getLikes().size());
            dto.setHasLiked(post.getLikes().stream()
                .anyMatch(like -> like.getUser().getUsername().equals(currentUsername)));
            dto.setLikes(post.getLikes().stream()
                .map(LikeDTO::fromEntity)
                .collect(Collectors.toSet()));
        }

        return dto;
    }
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