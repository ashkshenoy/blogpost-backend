package com.example.techblog.service;

import com.example.techblog.dto.AIPostRequest;
import com.example.techblog.dto.BlogPostDto;
import com.example.techblog.dto.BlogPostMapper;
import com.example.techblog.dto.CommentDto;
import com.example.techblog.dto.CreatePostRequest;
import com.example.techblog.model.BlogPost;
import com.example.techblog.model.Category;
import com.example.techblog.model.Comment;
import com.example.techblog.model.Like;
import com.example.techblog.model.Tag;
import com.example.techblog.model.User;
import com.example.techblog.repository.BlogPostRepository;
import com.example.techblog.repository.CategoryRepository;
import com.example.techblog.repository.CommentRepository;
import com.example.techblog.repository.TagRepository;
import com.example.techblog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository postRepo;
    private final CategoryRepository categoryRepo;
    private final TagRepository tagRepo;
    private final UserRepository userRepo;
    private final CommentRepository commentRepo;
    
    
    @Autowired
    AIService aiService;
    @Override
    public void assignCategory(Long postId, Long categoryId) {
        BlogPost post = postRepo.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        Category category = categoryRepo.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        Category categori = post.getCategory();
       // add to existing list
        post.setCategory(categori);

        postRepo.save(post);
    }

    @Override
    public void addTags(Long postId, List<Long> tagIds) {
        BlogPost post = postRepo.findById(postId).orElseThrow();
        Set<Tag> tags = new HashSet<>(tagRepo.findAllById(tagIds));
        post.getTags().addAll(tags);
        postRepo.save(post);
    }

    @Override
    public CommentDto addComment(Long postId, String content, String username) {
        BlogPost post = postRepo.findById(postId).orElseThrow();
        User user = userRepo.findByUsername(username);
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(user);
        comment.setContent(content);
        commentRepo.save(comment);
        return new CommentDto(comment.getId(), comment.getContent(), user.getUsername(), comment.getCreatedAt());
    }

    @Override
    public List<CommentDto> getComments(Long postId) {
        return commentRepo.findByPostId(postId).stream()
                .map(c -> new CommentDto(c.getId(), c.getContent(), c.getAuthor().getUsername(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }
    
    @Override
    public BlogPostDto createPost(CreatePostRequest request, String username) {
        User user = userRepo.findByUsername(username);
        System.out.println("Received request: " + request); // Debug log
        System.out.println("Category: " + request.getCategory()); // Debug log
        System.out.println("Tags: " + request.getTags());
        BlogPost post = new BlogPost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(user);
        
        // Handle single category
        if (request.getCategory() != null && !request.getCategory().trim().isEmpty()) {
            Category category = categoryRepo.findByName(request.getCategory())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategory());
                    return categoryRepo.save(newCategory);
                });
            post.setCategory(category);
        }
        
        // Handle tags
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            Set<Tag> tags = request.getTags().stream()
                .map((String tagName) -> {
                    return tagRepo.findByName(tagName)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(tagName);
                            return tagRepo.save(newTag);
                        });
                })
                .collect(Collectors.toSet());
            post.setTags(tags);
        }
          

        BlogPost saved = postRepo.save(post);
        return BlogPostMapper.toDto(saved);
    }
    @Override
    public BlogPostDto getPostById(Long id) {
        BlogPost post = postRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return BlogPostMapper.toDto(post);
    }

    @Override
    public List<BlogPostDto> getAllPosts() {
        return postRepo.findAll().stream()
                .map(BlogPostMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BlogPostDto updatePost(Long id, CreatePostRequest request) {
        BlogPost post = postRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        
        // Handle category update
        if (request.getCategory() != null && !request.getCategory().trim().isEmpty()) {
            Category category = categoryRepo.findByName(request.getCategory())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategory());
                    return categoryRepo.save(newCategory);
                });
            post.setCategory(category);
        }
        
        // Handle tags update
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            Set<Tag> tags = request.getTags().stream()
                .map(tagName -> tagRepo.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepo.save(newTag);
                    }))
                .collect(Collectors.toSet());
            post.setTags(tags);
        }

        BlogPost updated = postRepo.save(post);
        return BlogPostMapper.toDto(updated);
    }
    @Override
    public void deletePost(Long id) {
        postRepo.deleteById(id);
    }

    @Override
    public List<BlogPostDto> getPostsByCategory(String category) {
        List<BlogPost> posts = postRepo.findByCategory_Name(category);
        return posts.stream()
                    .map(BlogPostMapper::toDto)
                    .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostDto> getPostsByTag(Long tagId) {
        List<BlogPost> posts = postRepo.findByTagsId(tagId);
        return posts.stream()
                    .map(BlogPostMapper::toDto)
                    .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void likePost(Long postId, String username) {
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        
        boolean hasLiked = post.getLikes().stream()
                .anyMatch(like -> like.getUser().equals(user));
        
        if (!hasLiked) {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            post.getLikes().add(like);
            postRepo.save(post);
        }
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, String username) {
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepo.findByUsername(username);
        
        post.getLikes().removeIf(like -> like.getUser().equals(user));
        postRepo.save(post);
    }

    // ...exi
    
    @Override
    public BlogPostDto generateAIPost(AIPostRequest request, String authorUsername) {
        User author = userRepo.findByUsername(authorUsername);

        String aiContent = aiService.generatePost(request.getPrompt());

        BlogPost post = new BlogPost();
        post.setTitle(request.getTitle());
        post.setContent(aiContent);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());

        postRepo.save(post);
        return BlogPostMapper.toDto(post);
    }
    
    @Override
    public Page<BlogPostDto> searchPosts(String query, Long categoryId, Long tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<BlogPost> spec = Specification.where(null);

        if (query != null && !query.isEmpty()) {
            spec = spec.and((root, q, cb) ->
                cb.or(
                    cb.like(cb.lower(root.get("title")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("content")), "%" + query.toLowerCase() + "%")
                )
            );
        }

        if (categoryId != null) {
            spec = spec.and((root, q, cb) ->
                cb.equal(root.get("category").get("id"), categoryId)
            );
        }

        if (tagId != null) {
            spec = spec.and((root, q, cb) ->
                cb.isMember(tagRepo.getOne(tagId), root.get("tags"))
            );
        }
      
        return postRepo.findAll(spec, pageable).map(BlogPostMapper::toDto);
    }

}