package com.example.techblog.controller;

import com.example.techblog.dto.AIPostRequest;
import com.example.techblog.dto.BlogPostDto;
import com.example.techblog.dto.CommentDto;
import com.example.techblog.dto.CommentRequest;
import com.example.techblog.dto.CreatePostRequest;
import com.example.techblog.dto.LikeDTO;
import com.example.techblog.dto.LikeResponse;
import com.example.techblog.dto.UserDto;
import com.example.techblog.exception.PostNotFoundException;
import com.example.techblog.exception.UnauthorizedActionException;
import com.example.techblog.model.BlogPost;
import com.example.techblog.service.BlogPostService;
import com.example.techblog.service.LikeService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService postService;
    private final LikeService likeService;

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
    public ResponseEntity<List<BlogPostDto>> getAllPosts(
        @AuthenticationPrincipal(expression = "username") String username) {
        List<BlogPostDto> posts = postService.getAllPosts(username);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostDto> updatePost(@PathVariable Long id,
                                                  @RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }
	/*
	 * @PostMapping public ResponseEntity<BlogPostDto> createPost(@RequestBody
	 * CreatePostRequest request,
	 * 
	 * @AuthenticationPrincipal UserDetails userDetails) { String username =
	 * userDetails.getUsername(); BlogPostDto created =
	 * postService.createPost(request, username); return
	 * ResponseEntity.status(HttpStatus.CREATED).body(created); }
	 * 
	 * @GetMapping("/{id}") public ResponseEntity<BlogPostDto> getPost(@PathVariable
	 * Long id) { BlogPostDto post = postService.getPostById(id); if (post == null)
	 * { throw new PostNotFoundException("Post with ID " + id + " not found."); }
	 * return ResponseEntity.ok(post); }
	 */
    
	/*
	 * @PutMapping("/{id}") public ResponseEntity<BlogPostDto>
	 * updatePost(@PathVariable Long id,
	 * 
	 * @RequestBody CreatePostRequest request,
	 * 
	 * @AuthenticationPrincipal UserDetails userDetails) { BlogPost post =
	 * postService.getPostById(id); // Assuming this retrieves the post if (post ==
	 * null) { throw new PostNotFoundException("Post with ID " + id +
	 * " not found."); }
	 * 
	 * // Ensure only the author can update the post if
	 * (!post.getAuthor().getUsername().equals(userDetails.getUsername())) { throw
	 * new UnauthorizedActionException("You are not authorized to edit this post.");
	 * }
	 * 
	 * return ResponseEntity.ok(postService.updatePost(id, request)); }
	 */

	/*
	 * @DeleteMapping("/{id}") public ResponseEntity<Void> deletePost(@PathVariable
	 * Long id,
	 * 
	 * @AuthenticationPrincipal UserDetails userDetails) { BlogPost post =
	 * postService.getPostById(id); if (post == null) { throw new
	 * PostNotFoundException("Post with ID " + id + " not found."); }
	 * 
	 * if (!post.getAuthor().getUsername().equals(userDetails.getUsername())) {
	 * throw new
	 * UnauthorizedActionException("You are not authorized to delete this post."); }
	 * 
	 * postService.deletePost(id); return ResponseEntity.noContent().build(); }
	 */
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
    
	/*
	 * @PostMapping("/ai-generate") public BlogPostDto
	 * generatePostFromAI(@RequestBody AIPostRequest request,
	 * 
	 * @AuthenticationPrincipal UserDetails userDetails) { return
	 * postService.generateAIPost(request, userDetails.getUsername()); }
	 */
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
    public ResponseEntity<LikeResponse> likePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {  // Change this line
        try {
        	
            String username = userDetails.getUsername();  
            System.out.println("Username: "+ username);// Get username from UserDetails
            LikeResponse response = likeService.likePost(id, username);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LikeResponse(0, false));
        }
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<LikeResponse> unlikePost(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Unlike request received - ID: " + id + ", Username: " + username);
        return ResponseEntity.ok(likeService.unlikePost(id, username));
    }

    @GetMapping("/{id}/likes/status")
    public ResponseEntity<LikeResponse> getLikeStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(likeService.getLikeStatus(id, username));
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
    
    @DeleteMapping("/{postId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        postService.deleteComment(postId, commentId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
   
  
 // Static factory method to convert BlogPost entity to BlogPostDto
    public static BlogPostDto fromEntity(BlogPost post, String currentUsername) {
        if (post == null) return null;

        BlogPostDto dto = new BlogPostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthor(UserDto.fromEntity(post.getAuthor()));
        
        // Convert Category to String if needed
        dto.setCategory(post.getCategory() != null ? post.getCategory().getName() : null);
        
        dto.setTags(post.getTags() != null 
                ? post.getTags().stream()
                    .map(tag -> tag.getName())
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
}
