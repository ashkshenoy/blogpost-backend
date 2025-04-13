package com.example.techblog.service;

import java.util.List;

import org.springframework.data.domain.Page;
import com.example.techblog.dto.AIPostRequest;
import com.example.techblog.dto.BlogPostDto;
import com.example.techblog.dto.CommentDto;
import com.example.techblog.dto.CreatePostRequest;
import com.example.techblog.model.BlogPost;

public interface BlogPostService {
	 BlogPostDto createPost(CreatePostRequest request, String username);
	    BlogPostDto getPostById(Long id);
	    List<BlogPostDto> getAllPosts();
	    BlogPostDto updatePost(Long id, CreatePostRequest request);
	    void deletePost(Long id);
	    void assignCategory(Long postId, Long categoryId);
	    void addTags(Long postId, List<Long> tagIds);
	    CommentDto addComment(Long postId, String content, String username);
	    List<CommentDto> getComments(Long postId);
	    List<BlogPostDto> getPostsByCategory(String category);
	    List<BlogPostDto> getPostsByTag(Long tagId);
		BlogPostDto generateAIPost(AIPostRequest request, String authorUsername);
		 Page<BlogPostDto> searchPosts(
			        String query,
			        Long categoryId,
			        Long tagId,
			        int page,
			        int size
			    );
		void likePost(Long postId, String username);
		void unlikePost(Long postId, String username);
}
