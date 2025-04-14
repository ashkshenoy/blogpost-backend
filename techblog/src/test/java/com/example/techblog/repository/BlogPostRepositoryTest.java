package com.example.techblog.repository;

import com.example.techblog.model.BlogPost;
import com.example.techblog.model.Category;
import com.example.techblog.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BlogPostRepositoryTest {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should save and retrieve a blog post successfully")
    void shouldSaveAndFindBlogPost() {
        // Create and save a user
        User user = new User();
        user.setUsername("ash");
        user.setPassword("secret");
        user = userRepository.save(user);

        // Create and save a category
        Category category = new Category();
        category.setName("Tech");
        category = categoryRepository.save(category);

        // Create a blog post
        BlogPost post = new BlogPost();
        post.setTitle("Data JPA Test Post");
        post.setContent("Testing BlogPostRepository using @DataJpaTest.");
        post.setAuthor(user);
        post.setCategory(category);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        blogPostRepository.save(post);

        // Act: Fetch all blog posts
        List<BlogPost> found = blogPostRepository.findAll();

        // Assert
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("Data JPA Test Post");
        assertThat(found.get(0).getAuthor().getUsername()).isEqualTo("ash");
        assertThat(found.get(0).getCategory().getName()).isEqualTo("Tech");
    }
}
