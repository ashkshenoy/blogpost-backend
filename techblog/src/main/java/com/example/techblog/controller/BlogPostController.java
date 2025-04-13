package com.example.techblog.controller;

import com.example.techblog.dto.AIPostRequest;
import com.example.techblog.dto.BlogPostDto;
import com.example.techblog.dto.CommentDto;
import com.example.techblog.dto.CommentRequest;
import com.example.techblog.dto.CreatePostRequest;
import com.example.techblog.model.BlogPost;
import com.example.techblog.service.BlogPostService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService postService;

    @PostMapping
    public ResponseEntity<BlogPostDto> createPost(@RequestBody CreatePostRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername(); // Authenticated user
        BlogPostDto created = postService.createPost(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDto> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping
    public ResponseEntity<List<BlogPostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostDto> updatePost(@PathVariable Long id,
                                                  @RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/category/{categoryId}")
    public List<BlogPostDto> getPostsByCategory(@PathVariable String category) {
        return postService.getPostsByCategory(category);
    }

    @GetMapping("/tag/{tagId}")
    public List<BlogPostDto> getPostsByTag(@PathVariable Long tagId) {
        return postService.getPostsByTag(tagId);
    }

    

    @PostMapping("/{postId}/comment")
    public CommentDto addComment(@PathVariable Long postId,
                                 @RequestBody Map<String, String> body,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        return postService.addComment(postId, body.get("content"), userDetails.getUsername());
    }
    
    @PostMapping("/ai-generate")
    public BlogPostDto generatePostFromAI(@RequestBody AIPostRequest request,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        return postService.generateAIPost(request, userDetails.getUsername());
    }
    @GetMapping("/search")
    public Page<BlogPostDto> searchPosts(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Long tagId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        return postService.searchPosts(query, categoryId, tagId, page, size);
    }
    @PostMapping("/{id}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> likePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
    	postService.likePost(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> unlikePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
    	postService.unlikePost(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> addComment(
        @PathVariable Long id,
        @RequestBody CommentRequest request,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        CommentDto comment = postService.addComment(
            id, 
            request.getContent(), 
            userDetails.getUsername()
        );
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long id) {
        List<CommentDto> comments = postService.getComments(id);
        return ResponseEntity.ok(comments);
    }
}
