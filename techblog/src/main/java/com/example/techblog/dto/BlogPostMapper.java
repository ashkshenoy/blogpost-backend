package com.example.techblog.dto;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.example.techblog.model.BlogPost;
import com.example.techblog.model.Category;
import com.example.techblog.model.Tag;

public class BlogPostMapper {
    public static BlogPostDto toDto(BlogPost post) {
        if (post == null) return null;

        BlogPostDto dto = new BlogPostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthor(UserDto.fromEntity(post.getAuthor()));
        dto.setCategory(post.getCategory() != null ? post.getCategory().getName() : null);
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
            dto.setLikes(post.getLikes().stream()
                .map(LikeDTO::fromEntity)
                .collect(Collectors.toSet()));
        }

        return dto;
    }
}
	  

