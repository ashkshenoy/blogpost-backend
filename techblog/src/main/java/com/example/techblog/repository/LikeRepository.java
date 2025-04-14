package com.example.techblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.techblog.model.Like;
import com.example.techblog.model.BlogPost;
import com.example.techblog.model.User;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostAndUser(BlogPost post, User user);
    void deleteByPostAndUser(BlogPost post, User user);
	Optional<Like> findByPostAndUser(BlogPost post, User user);
}