package com.example.techblog.service;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.techblog.dto.LikeResponse;
import com.example.techblog.exception.ResourceNotFoundException;
import com.example.techblog.model.BlogPost;
import com.example.techblog.model.Like;
import com.example.techblog.model.User;
import com.example.techblog.repository.BlogPostRepository;
import com.example.techblog.repository.LikeRepository;
import com.example.techblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
public class LikeService {
    private final BlogPostRepository postRepo;
    private final UserRepository userRepo;
    private final LikeRepository likeRepo;

    @Autowired
    public LikeService(BlogPostRepository postRepo, UserRepository userRepo, LikeRepository likeRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.likeRepo = likeRepo;
    }

    public LikeResponse likePost(Long postId, String username) {
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
                
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }
        
        // Check if already liked
        boolean hasLiked = likeRepo.existsByPostAndUser(post, user);
        if (!hasLiked) {
            Like like = new Like();
            like.setPost(post);  // Set post reference
            like.setUser(user);  // Set user reference
            like.setCreatedAt(LocalDateTime.now());
            
            // Add to post's collection (bidirectional)
            post.getLikes().add(like);
            
            // Save post which will cascade to like due to CascadeType.ALL
            postRepo.save(post);
        }
        
        // Refresh post to get accurate count
        post = postRepo.findById(postId).get();
        return new LikeResponse(post.getLikes().size(), true);
    }

    public LikeResponse unlikePost(Long postId, String username) {
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
                
        User user = userRepo.findByUsername(username);
        System.out.println("Username: "+username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }
        
        // Find and remove the like
        Like like = likeRepo.findByPostAndUser(post, user)
                .orElseThrow(() -> new ResourceNotFoundException("Like not found"));
        
        // Remove from post's collection (bidirectional)
        post.getLikes().remove(like);
        
        // Save post which will handle orphan removal due to orphanRemoval=true
        postRepo.save(post);
        
        // Refresh post to get accurate count
        post = postRepo.findById(postId).get();
        return new LikeResponse(post.getLikes().size(), false);
    }

    @Transactional(readOnly = true)
    public LikeResponse getLikeStatus(Long postId, String username) {
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return new LikeResponse(post.getLikes().size(), false);
        }
        
        boolean hasLiked = likeRepo.existsByPostAndUser(post, user);
        return new LikeResponse(post.getLikes().size(), hasLiked);
    }
}