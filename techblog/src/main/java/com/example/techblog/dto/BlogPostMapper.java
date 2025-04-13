package com.example.techblog.dto;

import java.util.stream.Collectors;

import com.example.techblog.model.BlogPost;
import com.example.techblog.model.Category;
import com.example.techblog.model.Tag;

public class BlogPostMapper {

	public static BlogPostDto toDto(BlogPost post) {
	    BlogPostDto dto = new BlogPostDto();
	    dto.setId(post.getId());
	    dto.setTitle(post.getTitle());
	    dto.setContent(post.getContent());
	    dto.setCreatedAt(post.getCreatedAt());
	    dto.setUpdatedAt(post.getUpdatedAt());
	    
	    // Map author
	    if (post.getAuthor() != null) {
	        dto.setAuthor(new UserDto(
	            post.getAuthor().getId(),
	            post.getAuthor().getUsername()
	        ));
	    }
	    
	    // Map single category
	    if (post.getCategory() != null) {
	        dto.setCategory(post.getCategory().getName());
	    }
	    
	    // Map tags
	    if (post.getTags() != null) {
	        dto.setTags(post.getTags().stream()
	            .map(Tag::getName)
	            .collect(Collectors.toList()));
	    }
	    
	    return dto;
	}
	}
	  

